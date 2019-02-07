package tabdulin.demo.weather.forecast;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tabdulin.demo.weather.openweathermap.OWMException;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@RestController
@Api(description = "Weather Forecast Controller")
public class ForecastController {
    @Autowired
    private ForecastService forecastService;

    @ApiOperation(value = "Average forecast for the next 3 days")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Server internal error")
    })
    @GetMapping("/data")
    public List<Forecast> forecast(@RequestParam("city") String city) {
        return forecastService.forecast(city);
    }

    @ExceptionHandler
    public ResponseEntity exceptionHandler(Exception e) {
        log.error("Exception was thrown during forecasting weather", e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof ConstraintViolationException
                || e instanceof MissingServletRequestParameterException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof OWMException) {
            var resolved = HttpStatus.resolve(((OWMException) e).getStatus());
            if (resolved != null) {
                status = resolved;
            }
        }

        return ResponseEntity
                .status(status)
                .build();
    }

}
