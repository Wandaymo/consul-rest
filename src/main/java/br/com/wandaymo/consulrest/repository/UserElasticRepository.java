package br.com.wandaymo.consulrest.repository;

import br.com.wandaymo.consulrest.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserElasticRepository extends ElasticsearchRepository<User, String> {

    User findByUsername(String name);

}