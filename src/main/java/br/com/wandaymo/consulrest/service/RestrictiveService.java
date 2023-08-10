package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.api.dto.RestrictiveDTO;
import br.com.wandaymo.consulrest.entity.Restrictive;
import br.com.wandaymo.consulrest.mapper.RestrictiveMapper;
import br.com.wandaymo.consulrest.repository.RestrictiveElasticRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class RestrictiveService {

    private static final String RESTRICTIVE_NOT_FOUND = "Restrictive not found";

    @Autowired
    private RestrictiveElasticRepository restrictiveElasticRepository;


    public long count() {
        return restrictiveElasticRepository.count();
    }

    public Restrictive save(RestrictiveDTO restrictiveDTO) {
        var restrictive = RestrictiveMapper.INSTANCE.toRestrictive(restrictiveDTO);
        restrictive.setId(UUID.randomUUID().toString());
        return restrictiveElasticRepository.save(restrictive);
    }

    public Restrictive update(String restrictiveId, RestrictiveDTO restrictiveDTO) {
        if (restrictiveElasticRepository.findById(restrictiveId).isEmpty()) {
            throw new NotFoundException(RESTRICTIVE_NOT_FOUND);
        }
        var restrictive = RestrictiveMapper.INSTANCE.toRestrictive(restrictiveDTO);
        restrictive.setId(restrictiveId);
        return restrictiveElasticRepository.save(restrictive);
    }

    public Restrictive findById(String id) {
       return restrictiveElasticRepository.findById(id).orElseThrow(() -> new NotFoundException(RESTRICTIVE_NOT_FOUND));
    }

    public Page<Restrictive> findByPersonId(String personId, PageRequest pageable) {
        if (personId != null) {
            Page<Restrictive> restrictivePage = restrictiveElasticRepository.findByPersonId(personId, pageable);
            if (restrictivePage.isEmpty()) {
                throw new NotFoundException(RESTRICTIVE_NOT_FOUND);
            }
            return restrictivePage;
        }
        else {
            return restrictiveElasticRepository.findAll(pageable);
        }
    }

    public void deleteById(String id) {
        restrictiveElasticRepository.deleteById(id);
    }
}
