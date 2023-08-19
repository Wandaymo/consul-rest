package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.api.dto.UserDTO;
import br.com.wandaymo.consulrest.entity.User;
import br.com.wandaymo.consulrest.log.Logged;
import br.com.wandaymo.consulrest.mapper.UserMapper;
import br.com.wandaymo.consulrest.repository.UserElasticRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserElasticRepository userElasticRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Logged
    public String save(UserDTO userDTO) {
        if (userElasticRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new IllegalArgumentException("This username is already in use.");
        }

        var user = UserMapper.INSTANCE.toUser(userDTO);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userElasticRepository.save(user);

        return generateToken(userDTO);
    }

    @Logged
    public String getToken(UserDTO userDTO) {
        var userData = userElasticRepository.findByUsername(userDTO.getUsername());
        if (userData!= null && bCryptPasswordEncoder.matches(userDTO.getPassword(), userData.getPassword())) {
            return generateToken(userDTO);
        } else {
            throw new IllegalArgumentException("Unregistered user");
        }
    }

    private String generateToken(UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                        userDTO.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var user = (User) authenticate.getPrincipal();
        return tokenService.getToken(user);
    }
}
