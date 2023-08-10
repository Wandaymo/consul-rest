package br.com.wandaymo.consulrest.repository;

import br.com.wandaymo.consulrest.entity.Restrictive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictiveElasticRepository extends ElasticsearchRepository<Restrictive, String> {

    Page<Restrictive> findByPersonId(String personId, Pageable pageable);

}
