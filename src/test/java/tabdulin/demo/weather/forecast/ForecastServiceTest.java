package tabdulin.demo.weather.forecast;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import tabdulin.demo.weather.openweathermap.ForecastFactory;
import tabdulin.demo.weather.openweathermap.OWMException;
import tabdulin.demo.weather.openweathermap.OWMService;
import tabdulin.demo.weather.openweathermap.dto.Item;
import tabdulin.demo.weather.openweathermap.dto.Main;
import tabdulin.demo.weather.openweathermap.dto.OWMForecastResponse;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static tabdulin.demo.weather.openweathermap.ForecastFactory.City.BERLIN;

class ForecastServiceTest extends OWMServiceMockTest {
    public final String CITY_DUMMY = "dummy";

    @Autowired
    private ForecastService forecastService;

    @MockBean
    private Clock clock;

    @MockBean
    private OWMService owmService;

    @BeforeEach
    void beforeEach() throws IOException, OWMException {
        for (ForecastFactory.City city : ForecastFactory.City.values()) {
            OWMForecastResponse response = ForecastFactory.loadForecast(city);
            when(owmService.getForecast(city.name())).thenReturn(response);
        }

        Clock fixedClock = Clock.fixed(LocalDateTime
                        .of(2019, 2, 3, 8, 0, 0)
                        .toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    void getForecast_noCity_throwsForecastException() {
        assertThrows(ConstraintViolationException.class, () -> forecastService.forecast(null));
        assertThrows(ConstraintViolationException.class, () -> forecastService.forecast(""));
    }

    @Test
    void getForecast_dummyCity_success() {
        final double TEMPERATURE = 5.4;
        final double PRESSURE = 60.0;

        List<Item> items = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime time = now.plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);
        while (time.isBefore(now.plusDays(5))) {
            Item item = new Item();
            item.setDt(time.toInstant(ZoneOffset.UTC).getEpochSecond());
            Main main = new Main();
            main.setTemp(TEMPERATURE);
            main.setPressure(PRESSURE);
            item.setMain(main);
            items.add(item);
            time = time.plusHours(3);
        }

        OWMForecastResponse forecastResponse = new OWMForecastResponse();
        forecastResponse.setList(items);
        when(owmService.getForecast(CITY_DUMMY)).thenReturn(forecastResponse);

        var avg = forecastService.forecast(CITY_DUMMY);
        avg.forEach(weather -> {
            assertEquals(TEMPERATURE, weather.getAvgDayTemperature().doubleValue());
            assertEquals(TEMPERATURE, weather.getAvgNightTemperature().doubleValue());
            assertEquals(PRESSURE, weather.getAvgPressure().doubleValue());
        });
    }

    @Test
    void getForecast_Berlin_successful() {
        var response = forecastService.forecast(BERLIN.name());
        var first = response.get(0);
        assertEquals(3, response.size());
        assertEquals(LocalDate.of(2019, 2, 4), first.getDate());
        assertEquals(-0.42, first.getAvgNightTemperature().doubleValue());
        assertEquals(0.62, first.getAvgDayTemperature().doubleValue());
        assertEquals(1031.83, first.getAvgPressure().doubleValue());
    }

    @Test
    void round() {
        assertEquals(1.25, forecastService.round(1.24666).doubleValue());
        assertEquals(1.25, forecastService.round(1.245).doubleValue());
        assertEquals(1.24, forecastService.round(1.24333).doubleValue());
    }


    @Test
    void isNight() {
        assertTrue(forecastService.isNight(LocalDateTime.parse("2019-02-03T00:00:00")));
        assertTrue(forecastService.isNight(LocalDateTime.parse("2019-02-03T06:00:00")));
        assertTrue(forecastService.isNight(LocalDateTime.parse("2019-02-03T18:00:00")));
        assertFalse(forecastService.isNight(LocalDateTime.parse("2019-02-03T06:00:01")));
        assertFalse(forecastService.isNight(LocalDateTime.parse("2019-02-03T17:59:59")));
    }

    @Test
    void isDay() {
        assertTrue(forecastService.isDay(LocalDateTime.parse("2019-02-03T06:00:00")));
        assertTrue(forecastService.isDay(LocalDateTime.parse("2019-02-03T18:00:00")));
        assertFalse(forecastService.isDay(LocalDateTime.parse("2019-02-03T05:59:59")));
        assertFalse(forecastService.isDay(LocalDateTime.parse("2019-02-03T18:00:01")));
    }
}