package com.green.gajigaji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GajigajiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GajigajiApplication.class, args);
    }

}
