package com.cozycats.cozycatsbackend.admin.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan({"com.cozycats.cozycatscommon.entity", "com.cozycats.cozycatsbackend.user"})
public class CozyCatsBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(CozyCatsBackEndApplication.class, args);
    }

}
