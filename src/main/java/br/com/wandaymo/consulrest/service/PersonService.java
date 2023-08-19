package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.api.dto.PersonDTO;
import br.com.wandaymo.consulrest.entity.Person;
import br.com.wandaymo.consulrest.log.Logged;
import br.com.wandaymo.consulrest.mapper.PersonMapper;
import br.com.wandaymo.consulrest.repository.PersonElasticRepository;
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
public class PersonService {

    private static final String PERSON_NOT_FOUND = "Person not found";

    @Autowired
    private PersonElasticRepository personElasticRepository;

    @Logged
    public long count() {
        return personElasticRepository.count();
    }

    @Logged
    public Person save(PersonDTO personDTO) {
        var person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setId(UUID.randomUUID().toString());
        person.setCreatedDate(LocalDate.now());
        person.setCreatedBy(getLoggedUser());
        return personElasticRepository.save(person);
    }

    @Logged
    public Person update(String personId, PersonDTO personDTO) {
        Optional<Person> personOptional = personElasticRepository.findById(personId);
        if (personOptional.isEmpty()) {
            throw new NotFoundException(PERSON_NOT_FOUND);
        }
        var person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setId(personId);
        person.setCreatedDate(personOptional.get().getCreatedDate());
        person.setCreatedBy(personOptional.get().getCreatedBy());
        person.setUpdatedBy(getLoggedUser());
        person.setUpdatedDate(LocalDate.now());
        return personElasticRepository.save(person);
    }

    @Logged
    public Person findById(String id) {
        return personElasticRepository.findById(id).orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND));
    }

    @Logged
    public Page<Person> findByNameAndCpfCnpj(String name, String cpfCnpj, PageRequest pageable) {
        Page<Person> personPage = personElasticRepository.findByNameLikeAndCpfCnpj(replaceBlackSpace(name), cpfCnpj, pageable);
        if (personPage.isEmpty()) {
            throw new NotFoundException(PERSON_NOT_FOUND);
        }
        return personPage;
    }

    @Logged
    public void deleteById(String id) {
        if (personElasticRepository.findById(id).isEmpty()) {
            throw new NotFoundException(PERSON_NOT_FOUND);
        }
        personElasticRepository.deleteById(id);
    }

    private String replaceBlackSpace(String value) {
        return value.replace(" ", "%20");
    }
}
