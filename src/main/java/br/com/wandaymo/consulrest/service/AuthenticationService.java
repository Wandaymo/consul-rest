package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.log.Logged;
import br.com.wandaymo.consulrest.repository.UserElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserElasticRepository userElasticRepository;

    @Override
    @Logged
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userElasticRepository.findByUsername(username);
    }

    @Logged
    public static String getLoggedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
