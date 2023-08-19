package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.api.dto.RestrictiveDTO;
import br.com.wandaymo.consulrest.entity.Restrictive;
import br.com.wandaymo.consulrest.log.Logged;
import br.com.wandaymo.consulrest.mapper.RestrictiveMapper;
import br.com.wandaymo.consulrest.repository.RestrictiveElasticRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import static br.com.wandaymo.consulrest.service.AuthenticationService.getLoggedUser;

@Service
public class RestrictiveService {

    private static final String RESTRICTIVE_NOT_FOUND = "Restrictive not found";

    @Autowired
    private RestrictiveElasticRepository restrictiveElasticRepository;


    @Logged
    public long count() {
        return restrictiveElasticRepository.count();
    }

    @Logged
    public Restrictive save(RestrictiveDTO restrictiveDTO) {
        var restrictive = RestrictiveMapper.INSTANCE.toRestrictive(restrictiveDTO);
        restrictive.setId(UUID.randomUUID().toString());
        restrictive.setCreatedDate(LocalDate.now());
        restrictive.setCreatedBy(getLoggedUser());
        return restrictiveElasticRepository.save(restrictive);
    }

    @Logged
    public Restrictive update(String restrictiveId, RestrictiveDTO restrictiveDTO) {
        Optional<Restrictive> restrictiveOptional = restrictiveElasticRepository.findById(restrictiveId);
        if (restrictiveOptional.isEmpty()) {
            throw new NotFoundException(RESTRICTIVE_NOT_FOUND);
        }
        var restrictive = RestrictiveMapper.INSTANCE.toRestrictive(restrictiveDTO);
        restrictive.setId(restrictiveId);
        restrictive.setCreatedDate(restrictiveOptional.get().getCreatedDate());
        restrictive.setCreatedBy(restrictiveOptional.get().getCreatedBy());
        restrictive.setUpdatedBy(getLoggedUser());
        restrictive.setUpdatedDate(LocalDate.now());
        return restrictiveElasticRepository.save(restrictive);
    }

    @Logged
    public Restrictive findById(String id) {
       return restrictiveElasticRepository.findById(id).orElseThrow(() -> new NotFoundException(RESTRICTIVE_NOT_FOUND));
    }

    @Logged
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

    @Logged
    public void deleteById(String id) {
        if (restrictiveElasticRepository.findById(id).isEmpty()) {
            throw new NotFoundException(RESTRICTIVE_NOT_FOUND);
        }
        restrictiveElasticRepository.deleteById(id);
    }
}
