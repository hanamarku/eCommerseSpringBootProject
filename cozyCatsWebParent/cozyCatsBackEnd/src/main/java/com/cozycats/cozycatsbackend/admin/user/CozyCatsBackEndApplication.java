package com.cozycats.cozycatsbackend.admin.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.cozycats.cozycatscommon.entity", "com.cozycats.cozycatsbackend.admin.user"})
public class CozyCatsBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(CozyCatsBackEndApplication.class, args);
    }

}
