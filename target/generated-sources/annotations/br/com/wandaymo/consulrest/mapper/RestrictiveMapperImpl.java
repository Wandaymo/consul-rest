package br.com.wandaymo.consulrest.mapper;

import br.com.wandaymo.consulrest.api.dto.RestrictiveDTO;
import br.com.wandaymo.consulrest.entity.Restrictive;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-18T12:30:28-0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class RestrictiveMapperImpl extends RestrictiveMapper {

    @Override
    public Restrictive toRestrictive(RestrictiveDTO RestrictiveDTO) {
        if ( RestrictiveDTO == null ) {
            return null;
        }

        Restrictive restrictive = new Restrictive();

        restrictive.setPersonId( RestrictiveDTO.getPersonId() );
        restrictive.setType( RestrictiveDTO.getType() );
        restrictive.setDescription( RestrictiveDTO.getDescription() );
        restrictive.setMaximumValue( RestrictiveDTO.getMaximumValue() );
        restrictive.setMinimumValue( RestrictiveDTO.getMinimumValue() );

        return restrictive;
    }
}
