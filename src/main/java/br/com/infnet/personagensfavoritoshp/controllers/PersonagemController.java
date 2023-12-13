package br.com.infnet.personagensfavoritoshp.controllers;

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
@RequestMapping("/personagem")
public class PersonagemController {
    @Autowired
    PersonagemService personagemService;

    Logger logger = LoggerFactory.getLogger(PersonagemController.class);
    @GetMapping
    public ResponseEntity<?> getPersonagens(@RequestParam(required = false) Integer yearOfBirth, @RequestParam(required = false) String name ){
        List<Personagem> personagens;
        if(yearOfBirth != null){
            personagens = personagemService.getListByYear(yearOfBirth);
        } else if (name != null) {
            personagens = personagemService.getByPartialName(name);
        }
        else{
            personagens = personagemService.getAllPersonagem();
        }
        logger.info("GET ALL CHARACTERS");
        return new ResponseEntity<>(personagens, HttpStatus.OK);
    }


}
