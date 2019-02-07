# Demo Weather Forecast Application

## Task description

Using Java (1.8 or newer), create an API that will retrieve weather metrics of a specific city.
Please use ​ https://openweathermap.org/​ ​ to create a ​ free ​ account and retrieve the data for this
case study.

The requirements:
- The API should expose a ​ “/data”​ endpoint to retrieve the averages
- The​ “/data” ​ endpoint should return a JSON object that gives the averages of the
following metrics:
  - Average temperature (in Celsius)​ of the next 3 days from today’s date for ​ Day
time (06:00 – 18:00)​ and ​ Night time (18:00 – 06:00)​ .
  - Average of pressure for the next 3 days​ from today’s date.
- The​ “/data” ​ endpoint must include a CITY parameter containing the city’s name as the
input for the correct response.
- You ​ must use full REST API​ conventions. Correct error codes ​ must ​ be returned when
necessary.
- We ​ recommend ​ you to use Spring Boot.
- You ​ must ​ include unit tests.
- There ​ must ​ be a README file describing the process to run and test the API.
- There are many ways to solve this task, so please include in the README file your
reasoning and motivations behind your code, including why you chose to solve any
challenges in the way you did.


## Implementation

This application is a Spring boot application. 

### API

Request

`GET /data?city=<city-name>`

Response

```
[
    {
        "date": date of the forecast
        "avgDayTemperature": average temperature for the day (06:00 - 18:00)
        "avgNightTemperature": average temperature for the night (18:00 - 06:00)
        "avgPressure": average pressure
    }
]
```

Swagger api documentation available on [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
after application build and run

### Tools

- OpenJDK 11
- Apache Maven 3.6
- Spring Boot 2.1.2 
- Junit 5.3.1
- Docker 18.06 (optional)

## Build and run application

To run application you need to get an API_KEY from openweathermap.com. 

### Standalone java application

You need **OpenJDK 11** and **Maven 3.6** installed to run application in standalone mode.

To build an application run
`mvn clean package`

To run an application run 
`java -DOWM_API_KEY=<your-openweathermap-api-key> -jar target/weather.jar`

### Docker
You need the docker of version 17.05 and higher since Dockerfile contains multistage build.

`docker build -t tabdulin/weather . && docker run -e OWM_API_KEY=<your-openweathermap-api-key> -p 8080:8080 tabdulin/weather`

## Test application

To call an API linux curl command can be used, e.g:

`curl -X GET http://localhost:8080/data?berlin --header "Content-Type: application/json"` 

`curl -X GET http://localhost:8080/data?amsterdam --header "Content-Type: application/json"` 

`curl -X GET http://localhost:8080/data?astana --header "Content-Type: application/json"` 

## TODO

- more edge case process
- more detailed logging
- some temperature or pressure may be missed, process nulls


