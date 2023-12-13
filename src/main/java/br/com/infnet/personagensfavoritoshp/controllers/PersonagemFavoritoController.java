package br.com.infnet.personagensfavoritoshp.controllers;

import br.com.infnet.personagensfavoritoshp.exception.PersonagemNotFoundException;
import br.com.infnet.personagensfavoritoshp.model.Personagem;
import br.com.infnet.personagensfavoritoshp.service.PersonagemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personagens-favoritos")
public class PersonagemFavoritoController {

    @Autowired
    PersonagemService personagemService;

    Logger logger = LoggerFactory.getLogger(PersonagemController.class);
    @GetMapping
    public ResponseEntity<?> getPersonagens(@RequestParam(required = false) Integer yearOfBirth, @RequestParam(required = false) String name ){
        try {
            List<Personagem> personagens;
            if (yearOfBirth != null) {
                personagens = personagemService.getListByPersonagemFavoritoYear(yearOfBirth);
            } else if (name != null) {
                personagens = personagemService.getByPersonagemFavoritoPartialName(name);
            } else {
                personagens = personagemService.getAllPersonagemFavorito();
            }

            logger.info("GET ALL CHARACTERS");

            return personagens.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum personagem encontrado.") :
                    new ResponseEntity<>(personagens, HttpStatus.OK);
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao buscar personagens: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        try {
            logger.info("GET CHARACTER: " + id);
            Personagem personagem = personagemService.getByPersonagemFavoritoID(id);
            return new ResponseEntity<>(personagem, HttpStatus.OK);
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao buscar personagem: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByPartialName(@PathVariable String name) {
        try {
            logger.info("GET CHARACTER: " + name);
            List<Personagem> personagens = personagemService.getByPersonagemFavoritoPartialName(name);
            return ResponseEntity.ok().body(personagens);
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao buscar personagens: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/birth/{birthOfYear}")
    public ResponseEntity<?> getByBirthOfYear(@PathVariable int birthOfYear) {
        try {
            logger.info("GET CHARACTER: " + birthOfYear);
            List<Personagem> personagens = personagemService.getListByPersonagemFavoritoYear(birthOfYear);
            return ResponseEntity.ok().body(personagens);
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao buscar personagens: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Personagem personagem) {
        try {
            logger.info("CREATE CHARACTER: " + personagem);
            personagemService.create(personagem);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            String errorMessage = "Erro ao criar personagem: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            logger.info("DELETE CHARACTER: " + id);
            personagemService.delete(id);
            logger.info("PERSONAGEM DELETADO: " + id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao deletar personagem: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Personagem personagem) {
        try {
            logger.info("UPDATE PERSONAGEM: " + id + " novo personagem: " + personagem);
            personagemService.update(id, personagem);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (PersonagemNotFoundException e) {
            logger.warn("Personagem não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personagem não encontrado.");
        } catch (Exception e) {
            String errorMessage = "Erro ao atualizar personagem: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}