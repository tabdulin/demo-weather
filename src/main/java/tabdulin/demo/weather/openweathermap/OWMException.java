package tabdulin.demo.weather.openweathermap;

import lombok.Getter;

public class OWMException extends RuntimeException {
    @Getter
    private final int status;

    public OWMException(int status) {
        this.status = status;
    }
}
