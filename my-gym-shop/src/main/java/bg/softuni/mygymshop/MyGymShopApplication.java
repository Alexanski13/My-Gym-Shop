package bg.softuni.mygymshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyGymShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyGymShopApplication.class, args);
    }

}
