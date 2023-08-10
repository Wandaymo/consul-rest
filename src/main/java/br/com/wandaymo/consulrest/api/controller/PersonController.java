package br.com.wandaymo.consulrest.api.controller;

import br.com.wandaymo.consulrest.api.dto.PersonDTO;
import br.com.wandaymo.consulrest.entity.Person;
import br.com.wandaymo.consulrest.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/person")
@RestController
@Tag(name = "Person API", description = "Documentation for Person API")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> countPerson() {
        return ResponseEntity.ok("There are " + personService.count() + " persons");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Person createNewPerson(@RequestBody PersonDTO personDto) {
        return personService.save(personDto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findPersonByID(@PathVariable("id") String personId) {
        return personService.findById(personId);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Person updatePerson(@PathVariable("id") String personId, @RequestBody PersonDTO updatedPerson) {
        return personService.update(personId, updatedPerson);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findPersonsByParam(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "cpfCnpj") String cpfCnpj,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        return personService.findByNameAndCpfCnpj(name, cpfCnpj, pageable).getContent();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String id) {
        personService.deleteById(id);
    }
}
