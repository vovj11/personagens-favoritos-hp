package br.com.infnet.personagensfavoritoshp.service;

import br.com.infnet.personagensfavoritoshp.exception.PersonagemNotFoundException;
import br.com.infnet.personagensfavoritoshp.model.Personagem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PersonagemService {
    Logger logger = LoggerFactory.getLogger(PersonagemService.class);
    private List<Personagem> personagens = initPersonagem();
    private List<Personagem> personagensFavoritos = initPersonagensFavoritos();

    private List<Personagem> initPersonagem() {
        List<Personagem> personagens = new ArrayList<>();
        final String uri = "https://hp-api.onrender.com/api/characters";
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        int contadorId = 1;
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            logger.info("Status code: " + ((ResponseEntity<?>) response).getStatusCode());
            String result = response.getBody();
            try {
                JsonNode root = mapper.readTree(result);

                for (JsonNode personagemNode : root) {
                    Personagem personagem = mapper.readValue(personagemNode.toString(), Personagem.class);
                    personagem.setPersonagemId(contadorId++);
                    personagens.add(personagem);
                }
            } catch (Exception e) {
                logger.error("Error processing JSON", e);
            }

        return personagens;
    }

private List<Personagem> initPersonagensFavoritos() {
        List<Personagem> result = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
        if (i < personagens.size()) {
            result.add(personagens.get(i));
        }
    }
    return result;
}
    public List<Personagem> getAllPersonagem(){
        return personagens;
    }
    public Personagem getByID(int id){
        return personagens.get(id);
    }
    public List<Personagem> getByPartialName(String partialName) {
        String partialNameLowerCase = partialName.toLowerCase();

        return personagens.stream()
                .filter(personagem -> personagem.getName().toLowerCase().contains(partialNameLowerCase))
                .collect(Collectors.toList());
    }

    public List<Personagem> getListByYear(int yearOfBirth){
        return personagens.stream()
                .filter(personagem -> personagem.getYearOfBirth() == yearOfBirth)
                .collect(Collectors.toList());
    }


    public List<Personagem> getAllPersonagemFavorito(){
        return personagensFavoritos;
    }
    public Personagem getByPersonagemFavoritoID(int id){
        return personagensFavoritos.get(id-1);
    }
    public List<Personagem> getByPersonagemFavoritoPartialName(String partialName) {
        String partialNameLowerCase = partialName.toLowerCase();

        List<Personagem> personagensEncontrados = personagensFavoritos.stream()
                .filter(personagem -> personagem.getName().toLowerCase().contains(partialNameLowerCase))
                .collect(Collectors.toList());

        if (personagensEncontrados.isEmpty()) {
            throw new PersonagemNotFoundException("Nenhum personagem encontrado com o nome " + partialName);
        }

        return personagensEncontrados;
    }

    public List<Personagem> getListByPersonagemFavoritoYear(int yearOfBirth){
        List<Personagem> personagensEncontrados = personagensFavoritos.stream()
                .filter(personagem -> personagem.getYearOfBirth() == yearOfBirth)
                .collect(Collectors.toList());

        if (personagensEncontrados.isEmpty()) {
            throw new PersonagemNotFoundException("Nenhum personagem encontrado nascido no ano " + yearOfBirth);
        }

        return personagensEncontrados;
    }

    public void create(Personagem novoPersonagem){

        boolean idExistente = personagensFavoritos.stream()
                .anyMatch(personagem -> personagem.getPersonagemId() == novoPersonagem.getPersonagemId());

        if (idExistente) {
            throw new IllegalArgumentException("Já existe um personagem com esse Id");
        }

        personagensFavoritos.add(novoPersonagem);
    }
    public void delete(int id){
        personagensFavoritos.remove(id-1);
    }
    public void update(int id, Personagem personagem){
        personagensFavoritos.remove(id-1);
        personagem.setPersonagemId(id);
        personagensFavoritos.add(id-1, personagem);
    }

}
