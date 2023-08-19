package br.com.wandaymo.consulrest.mapper;

import br.com.wandaymo.consulrest.api.dto.UserDTO;
import br.com.wandaymo.consulrest.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-18T12:30:28-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User toUser(UserDTO user) {
        if ( user == null ) {
            return null;
        }

        User user1 = new User();

        user1.setUsername( user.getUsername() );
        user1.setPassword( user.getPassword() );

        return user1;
    }
}
