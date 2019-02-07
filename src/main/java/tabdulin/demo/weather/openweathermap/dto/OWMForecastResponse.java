package tabdulin.demo.weather.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OWMForecastResponse extends OWMAbstractResponse {
    private City city;
    private List<Item> list;
}
