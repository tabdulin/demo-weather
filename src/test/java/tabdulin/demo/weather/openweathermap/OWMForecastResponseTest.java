package tabdulin.demo.weather.openweathermap;

import org.junit.jupiter.api.Test;
import tabdulin.demo.weather.openweathermap.dto.Item;
import tabdulin.demo.weather.openweathermap.dto.OWMForecastResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tabdulin.demo.weather.openweathermap.ForecastFactory.City.ASTANA;
import static tabdulin.demo.weather.openweathermap.ForecastFactory.City.BERLIN;

public class OWMForecastResponseTest {

    @Test
    void mappingBerlin() throws IOException {
        OWMForecastResponse response = ForecastFactory.loadForecast(BERLIN);
        assertEquals("Berlin", response.getCity().getName());

        assertEquals(37, response.getList().size());

        assertEquals("Berlin", response.getCity().getName());
        assertEquals("DE", response.getCity().getCountry());
        Item first = response.getList().get(0);
        assertEquals(LocalDateTime.of(2019,2,3, 9, 0, 0), first.getTime());
        assertEquals(1.36, first.getMain().getTemp().doubleValue());
        assertEquals(1017.13, first.getMain().getPressure().doubleValue());
    }

    @Test
    void mappingAstana() throws IOException {
        OWMForecastResponse response = ForecastFactory.loadForecast(ASTANA);
        assertEquals("Astana", response.getCity().getName());
        assertEquals("KZ", response.getCity().getCountry());

        assertEquals(37, response.getList().size());
        Item first = response.getList().get(0);
        assertEquals(LocalDateTime.of(2019,2,3, 9, 0, 0), first.getTime());
        assertEquals(-10.38, first.getMain().getTemp().doubleValue());
        assertEquals(996.14, first.getMain().getPressure().doubleValue());
    }

}
