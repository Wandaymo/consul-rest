package br.com.wandaymo.consulrest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestrictiveDTO {

    private String personId;
    private String type;
    private String description;
    private Double maximumValue;
    private Double minimumValue;

}
