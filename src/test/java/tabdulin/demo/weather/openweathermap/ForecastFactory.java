package tabdulin.demo.weather.openweathermap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tabdulin.demo.weather.openweathermap.dto.OWMForecastResponse;

import java.io.IOException;

public class ForecastFactory {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public static OWMForecastResponse loadForecast(City city) throws IOException {
        return mapper.readValue(OWMService.class.getResourceAsStream(city.json), OWMForecastResponse.class);
    }

    @AllArgsConstructor
    public enum City {
        BERLIN("berlin.json"),
        ASTANA("astana.json");

        @Getter
        private String json;
    }
}
