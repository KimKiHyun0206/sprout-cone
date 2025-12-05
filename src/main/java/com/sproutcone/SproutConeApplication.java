package com.sproutcone;

import com.sproutcone.config.properties.JWTProperties;
import com.sproutcone.config.properties.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({
        KakaoProperties.class,
        JWTProperties.class
})
@SpringBootApplication
public class SproutConeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SproutConeApplication.class, args);
    }

}
