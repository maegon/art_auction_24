package com.example.ArtAuction_24.global.base.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("tests")
public class TestInitData implements BeforeInitData {
    @Bean
    CommandLineRunner init() {
        return args -> {
            beforeInit();
        };
    }
}
