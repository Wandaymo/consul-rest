package br.com.wandaymo.consulrest.repository;

import br.com.wandaymo.consulrest.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonElasticRepository extends ElasticsearchRepository<Person, String> {

    Page<Person> findByNameLikeAndCpfCnpj(String name, String cpfCnpj, Pageable pageable);

}
