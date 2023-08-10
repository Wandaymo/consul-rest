package br.com.wandaymo.consulrest.service;

import br.com.wandaymo.consulrest.api.dto.PersonDTO;
import br.com.wandaymo.consulrest.entity.Person;
import br.com.wandaymo.consulrest.mapper.PersonMapper;
import br.com.wandaymo.consulrest.repository.PersonElasticRepository;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class PersonService {

    private static final String PERSON_NOT_FOUND = "Person not found";

    @Autowired
    private PersonElasticRepository personElasticRepository;

    public long count() {
        return personElasticRepository.count();
    }

    public Person save(PersonDTO personDTO) {
        var person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setCreatedDate(LocalDate.now());
        person.setId(UUID.randomUUID().toString());
        return personElasticRepository.save(person);
    }

    public Person update(String personId, PersonDTO personDTO) {
        if (personElasticRepository.findById(personId).isEmpty()) {
            throw new NotFoundException(PERSON_NOT_FOUND);
        }
        var person = PersonMapper.INSTANCE.toPerson(personDTO);
        person.setId(personId);
        return personElasticRepository.save(person);
    }

    public Person findById(String id) {
        return personElasticRepository.findById(id).orElseThrow(() -> new NotFoundException(PERSON_NOT_FOUND));
    }

    public Page<Person> findByNameAndCpfCnpj(String name, String cpfCnpj, PageRequest pageable) {
        Page<Person> personPage = personElasticRepository.findByNameLikeAndCpfCnpj(replaceBlackSpace(name), cpfCnpj, pageable);
        if (personPage.isEmpty()) {
            throw new NotFoundException(PERSON_NOT_FOUND);
        }
        return personPage;
    }

    public void deleteById(String id) {
        personElasticRepository.deleteById(id);
    }

    private String replaceBlackSpace(String value) {
        return value.replace(" ", "%20");
    }
}
