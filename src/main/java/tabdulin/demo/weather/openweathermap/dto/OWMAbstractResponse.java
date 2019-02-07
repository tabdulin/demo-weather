package tabdulin.demo.weather.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class OWMAbstractResponse {
    private Integer cod;
    private String message;
}
