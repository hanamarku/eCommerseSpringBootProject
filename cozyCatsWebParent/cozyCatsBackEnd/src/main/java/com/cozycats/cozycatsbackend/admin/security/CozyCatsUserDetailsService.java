package com.cozycats.cozycatsbackend.admin.security;

import com.cozycats.cozycatsbackend.admin.user.UserRepository;
import com.cozycats.cozycatscommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CozyCatsUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if(user != null){
            return new CozyCatsUserDetails(user);
        }
        throw new UsernameNotFoundException("Could not find user with email: "+ email);
    }
}
