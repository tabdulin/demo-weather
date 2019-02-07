package tabdulin.demo.weather.openweathermap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OWMServiceTest {
    @Autowired
    private OWMService owmService;

    @Test
    void getForecast_BerlinPassed_success() throws OWMException {
        var response = owmService.getForecast("Berlin,DE");
        assertEquals(200, response.getCod().intValue());
    }
}