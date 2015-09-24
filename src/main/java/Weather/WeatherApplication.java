package Weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Weather.WeatherController;

@SpringBootApplication
public class WeatherApplication {

    public static void main(String[] args) {
    	WeatherController wCtrl = new WeatherController();
        SpringApplication.run(WeatherApplication.class, args);
    }
}
