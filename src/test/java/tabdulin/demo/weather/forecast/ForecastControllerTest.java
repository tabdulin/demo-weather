package tabdulin.demo.weather.forecast;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import tabdulin.demo.weather.openweathermap.ForecastFactory;
import tabdulin.demo.weather.openweathermap.OWMException;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ForecastControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ForecastService forecastService;

    @Test
    void forecast_berlin_ok() {
        String city = ForecastFactory.City.BERLIN.name();
        when(forecastService.forecast(city)).thenReturn(new LinkedList<>());

        String url = UriComponentsBuilder.fromUriString("/data")
                .queryParam("city", city)
                .build()
                .toUriString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    void forecast_noCity_badRequest() {
        when(forecastService.forecast(any())).thenThrow(new ConstraintViolationException(new HashSet<>()));

        String url = UriComponentsBuilder.fromUriString("/data")
                .build()
                .toUriString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void forecast_notExistingCity_notFound() {
        when(forecastService.forecast(any())).thenThrow(new OWMException(404));

        String url = UriComponentsBuilder.fromUriString("/data")
                .queryParam("city", "someNotExistingCity")
                .build()
                .toUriString();
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

}