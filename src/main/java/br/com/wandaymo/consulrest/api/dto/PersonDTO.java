package br.com.wandaymo.consulrest.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private String name;

    @Field(type = FieldType.Date, format = DateFormat.date)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    private LocalDate bornDate;

    private String personType;

    private String cpfCnpj;

    private String registerNumber;

    private String email;

    private String cep;

    private String address;

    private String addressNumber;

    private String district;

    private String city;

    private String state;

    private String phoneNumber;

}
