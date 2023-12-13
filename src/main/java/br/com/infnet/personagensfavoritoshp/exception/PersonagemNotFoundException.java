package br.com.infnet.personagensfavoritoshp.exception;

public class PersonagemNotFoundException extends RuntimeException {
    public PersonagemNotFoundException(String message) {
        super(message);
    }
}
