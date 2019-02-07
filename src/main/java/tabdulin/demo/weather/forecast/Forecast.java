package tabdulin.demo.weather.forecast;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class Forecast {
    @NotNull
    @ApiModelProperty(value = "Date of the forecast")
    private LocalDate date;

    @ApiModelProperty(value = "Average day time temperature")
    private Double avgDayTemperature;

    @ApiModelProperty(value = "Average night time temperature")
    private Double avgNightTemperature;

    @ApiModelProperty(value = "Average date pressure")
    private Double avgPressure;
}
