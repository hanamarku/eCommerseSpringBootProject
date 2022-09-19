package com.cozycats.cozycatsbackend.admin;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

//    public void testEncodePassword(){
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String rawPassword = "nam2020";
//        String encodePassword = passwordEncoder.encode(rawPassword);
//        System.out.println(encodePassword);
//
//        boolean matches = passwordEncoder.matches(rawPassword, encodePassword);
//       assertThat(matches).isTrue();
//    }


        @Test
        public void testBCryptPasswordEncoder() {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String rawPassword = "alex2020";
            String encodedPassword = encoder.encode(rawPassword);

            System.out.println("Encoded password: " + encodedPassword);

            boolean matched = encoder.matches(rawPassword, encodedPassword);

            assertTrue(matched);
        }

}
