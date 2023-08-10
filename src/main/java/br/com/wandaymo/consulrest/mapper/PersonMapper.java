package br.com.wandaymo.consulrest.mapper;

import br.com.wandaymo.consulrest.api.dto.PersonDTO;
import br.com.wandaymo.consulrest.entity.Person;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = PersonMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PersonMapper {

    public static final PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    public abstract Person toPerson(PersonDTO personDTO);
}
