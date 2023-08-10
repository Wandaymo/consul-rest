package br.com.wandaymo.consulrest.mapper;

import br.com.wandaymo.consulrest.api.dto.PersonDTO;
import br.com.wandaymo.consulrest.entity.Person;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-10T17:36:23-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class PersonMapperImpl extends PersonMapper {

    @Override
    public Person toPerson(PersonDTO personDTO) {
        if ( personDTO == null ) {
            return null;
        }

        Person person = new Person();

        person.setName( personDTO.getName() );
        person.setBornDate( personDTO.getBornDate() );
        person.setPersonType( personDTO.getPersonType() );
        person.setCpfCnpj( personDTO.getCpfCnpj() );
        person.setRegisterNumber( personDTO.getRegisterNumber() );
        person.setEmail( personDTO.getEmail() );
        person.setCep( personDTO.getCep() );
        person.setAddress( personDTO.getAddress() );
        person.setAddressNumber( personDTO.getAddressNumber() );
        person.setDistrict( personDTO.getDistrict() );
        person.setCity( personDTO.getCity() );
        person.setState( personDTO.getState() );
        person.setPhoneNumber( personDTO.getPhoneNumber() );

        return person;
    }
}
