package br.com.wandaymo.consulrest.mapper;

import br.com.wandaymo.consulrest.api.dto.RestrictiveDTO;
import br.com.wandaymo.consulrest.entity.Restrictive;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = RestrictiveMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class RestrictiveMapper {

    public static final RestrictiveMapper INSTANCE = Mappers.getMapper(RestrictiveMapper.class);

    public abstract Restrictive toRestrictive(RestrictiveDTO RestrictiveDTO);
}
