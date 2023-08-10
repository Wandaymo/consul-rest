package br.com.wandaymo.consulrest.api.controller;

import br.com.wandaymo.consulrest.api.dto.RestrictiveDTO;
import br.com.wandaymo.consulrest.entity.Restrictive;
import br.com.wandaymo.consulrest.service.RestrictiveService;
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

@RequestMapping("/v1/restrictive")
@RestController
@Tag(name = "Restrictive API", description = "Documentation for Restrictive API")
public class RestrictiveController {

    @Autowired
    private RestrictiveService restrictiveService;

    @GetMapping(value = "/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> countRestrictive() {
        return ResponseEntity.ok("There are " + restrictiveService.count() + " restrictives");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Restrictive createNewRestrictive(@RequestBody RestrictiveDTO restrictiveDTO) {
        return restrictiveService.save(restrictiveDTO);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restrictive> findRestrictiveByID(@PathVariable("id") String restrictiveId) {
        return ResponseEntity.ok(restrictiveService.findById(restrictiveId));
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Restrictive updateRestrictive(@PathVariable("id") String restrictiveId, @RequestBody RestrictiveDTO updatedRestrictiveDTO) {
        return restrictiveService.update(restrictiveId, updatedRestrictiveDTO);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restrictive> findRestrictivesByParam(@RequestParam(name = "personId") String personId,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size);
        return restrictiveService.findByPersonId(personId, pageable).getContent();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestrictive(@PathVariable String id) {
        restrictiveService.deleteById(id);
    }
}

