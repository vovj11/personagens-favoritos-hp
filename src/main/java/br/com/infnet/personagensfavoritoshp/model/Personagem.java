package br.com.infnet.personagensfavoritoshp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data@JsonIgnoreProperties(ignoreUnknown = true)
public class Personagem {
    private int personagemId;
    private String name;
    private int yearOfBirth;
    private List<String> alternate_names;


}
