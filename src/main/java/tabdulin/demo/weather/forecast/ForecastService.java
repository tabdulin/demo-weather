package tabdulin.demo.weather.forecast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tabdulin.demo.weather.openweathermap.OWMException;
import tabdulin.demo.weather.openweathermap.OWMService;
import tabdulin.demo.weather.openweathermap.dto.Item;
import tabdulin.demo.weather.openweathermap.dto.OWMForecastResponse;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.ToDoubleFunction;

@Slf4j
@Service
@Validated
public class ForecastService {
    @Autowired
    private OWMService owmService;

    @Autowired
    private Clock clock;

    /**
     * Retrieves weather forecast data for the next 3 days for the specified city
     *
     * @param city city name, e.g. berlin, amsterdam, londong, etc.
     *
     * @return Forecast object, one per one day
     *
     * @throws OWMException if there were some problems while getting result from openweathermap.com
     */
    public List<Forecast> forecast(@NotBlank String city) {
        log.info("City: {} - Getting average forecasts", city);
        OWMForecastResponse forecast = owmService.getForecast(city);
        LocalDate today = LocalDate.now(clock);
        int FORECAST_DAYS_NUMBER = 3;
        List<Forecast> averages = new ArrayList<>(FORECAST_DAYS_NUMBER);
        for (int i = 1; i <= FORECAST_DAYS_NUMBER; i++) {
            LocalDate date = today.plusDays(i);
            Forecast average = new Forecast();
            average.setDate(date);
            // TODO: optimize later, iterating multiple times but collection is small
            average.setAvgDayTemperature(average(forecast.getList(), date, true, item -> item.getMain().getTemp()));
            average.setAvgNightTemperature(average(forecast.getList(), date, false, item -> item.getMain().getTemp()));
            average.setAvgPressure(average(forecast.getList(), date, null, item -> item.getMain().getPressure()));
            averages.add(average);
        }

        log.info("City: {} - Getting average forecasts finished", city);
        return averages;
    }

    Double average(List<Item> items, LocalDate date, Boolean day, ToDoubleFunction<Item> mapper) {
        try {
            return round(items.stream()
                    .filter(item -> item.getTime().toLocalDate().equals(date))
                    .filter(item -> day == null || (day ? isDay(item.getTime()) : isNight(item.getTime())))
                    .mapToDouble(mapper)
                    .average()
                    .orElseThrow());
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    boolean isNight(LocalDateTime time) {
        return time.getHour() < 6
                || (time.getHour() == 6 && time.getMinute() == 0 && time.getSecond() == 0 && time.getNano() == 0)
                || time.getHour() >= 18;
    }

    boolean isDay(LocalDateTime time) {
        return time.getHour() >= 6
                && time.getHour() < 18
                || (time.getHour() == 18 && time.getMinute() == 0 && time.getSecond() == 0 && time.getNano() == 0);
    }

    Double round(Double number) {
        return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
