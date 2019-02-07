package tabdulin.demo.weather.openweathermap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tabdulin.demo.weather.openweathermap.dto.OWMAbstractResponse;
import tabdulin.demo.weather.openweathermap.dto.OWMForecastResponse;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Arrays;

@Slf4j
@Component
@Validated
public class OWMService {
    @Value("${app.openweathermap.api.baseUrl}")
    private String apiBaseUrl;

    @Value("${app.openweathermap.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate owmRestClient;

    /**
     * Retrieves weathr forecast data for the city from openweathermap.com
     *
     * @see <a href="https://openweathermap.org/forecast5">Open Weather Map API</a>
     *
     * @param city city name, e.g. berlin, london, amsterdam
     *
     * @return Basic version of response (see full response <a href="https://openweathermap.org/forecast5#JSON">here</a>)
     *
     * @throws OWMException
     */
    @Cacheable(cacheNames = "forecasts", key = "#city")
    public OWMForecastResponse getForecast(@NotNull String city) {
        log.info("City: {} - Getting forecast", city);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        URI uri = UriComponentsBuilder.fromUriString(apiBaseUrl + "/forecast")
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .queryParam("q", city)
                .build()
                .toUri();

        RequestEntity request = new RequestEntity(headers, HttpMethod.GET, uri);

        log.info("Requesting for: {}", uri);
        ResponseEntity<? extends OWMAbstractResponse> response = owmRestClient.exchange(request, OWMForecastResponse.class);

        if (HttpStatus.OK != response.getStatusCode()
                || HttpStatus.OK.value() != response.getBody().getCod()) {
            log.info("City: {} - Getting forecast failed: ", city, response.getBody().getCod());
            throw new OWMException(response.getBody().getCod());
        }

        log.info("City: {} - Getting forecast finished", city);
        return (OWMForecastResponse) response.getBody();
    }
}

