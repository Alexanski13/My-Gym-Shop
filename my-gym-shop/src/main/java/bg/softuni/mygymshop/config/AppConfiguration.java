package bg.softuni.mygymshop.config;

import bg.softuni.mygymshop.scheduleJob.ProductMonitoringJob;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
