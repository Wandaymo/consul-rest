package br.com.wandaymo.consulrest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "restrictive")
public class Restrictive {

    @Id
    private String id;
    private String personId;
    private String type;
    private String description;
    private Double maximumValue;
    private Double minimumValue;

}
