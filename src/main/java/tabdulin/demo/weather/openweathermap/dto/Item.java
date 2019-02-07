package tabdulin.demo.weather.openweathermap.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private Long dt;
    private Main main;

    // TODO: TIMEZONES
    // TODO: write custom serializer
    public LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.of("UTC"));
    }
}
