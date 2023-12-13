package br.com.infnet.personagensfavoritoshp;

import br.com.infnet.personagensfavoritoshp.model.Personagem;
import br.com.infnet.personagensfavoritoshp.service.PersonagemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("PersonagemService Testes")
public class PersonagemServiceTest {

    @Autowired
    PersonagemService personagemService;

    @Test
    @DisplayName("Get All Personagens")
    void testGetAllPersonagem() {
        assertNotNull(personagemService.getAllPersonagem());
        assertFalse(personagemService.getAllPersonagem().isEmpty());
    }

    @Test
    @DisplayName("Get Personagem By ID")
    void testGetByID() {
        int id = 1;
        assertEquals(personagemService.getByID(1), personagemService.getByID(id));
    }

    @Test
    @DisplayName("Get Personagem By ID inexistente. Espera IndexOutOfBoundsException")
    void testGetByNonExistentID() {
        int idInexistente = 1000;
        assertThrows(IndexOutOfBoundsException.class, () -> personagemService.getByID(idInexistente));
    }

    @Test
    @DisplayName("Get Personagens By Partial Name")
    void testGetByPartialName() {
        String partialName = "Harry";
        assertFalse(personagemService.getByPartialName(partialName).isEmpty());
    }

    @Test
    @DisplayName("Get Personagens By Partial Name Inexistente")
    void testGetByNonExistentPartialName() {
        String inexistentePartialName = "NonExistentName";
        assertTrue(personagemService.getByPartialName(inexistentePartialName).isEmpty());
    }

    @Test
    @DisplayName("Get Personagens By Year of Birth/Ano de nascimento")
    void testGetListByYear() {
        int yearOfBirth = 1980;
        assertFalse(personagemService.getListByYear(yearOfBirth).isEmpty());
    }

    @Test
    @DisplayName("Get All Personagens Favoritos")
    void testGetAllPersonagemFavorito() {
        assertNotNull(personagemService.getAllPersonagemFavorito());
        assertFalse(personagemService.getAllPersonagemFavorito().isEmpty());
    }

    @Test
    @DisplayName("Get Personagem Favorito By ID")
    void testGetByPersonagemFavoritoID() {
        int id = 1;
        assertNotNull(personagemService.getByPersonagemFavoritoID(id));
        assertEquals(id, personagemService.getByPersonagemFavoritoID(id).getPersonagemId());
    }

    @Test
    @DisplayName("Get Personagem Favorito By ID inexistente. Espera IndexOutOfBoundsException")
    void testGetByNonExistentPersonagemFavoritoID() {
        int idInexistente = 1000;
        assertThrows(IndexOutOfBoundsException.class, () -> personagemService.getByPersonagemFavoritoID(idInexistente));
    }

    @Test
    @DisplayName("Get Personagens Favoritos By Partial Name")
    void testGetByPersonagemFavoritoPartialName() {
        String partialName = "Harry";
        List<Personagem> personagens = personagemService.getByPersonagemFavoritoPartialName(partialName);
        assertFalse(personagens.isEmpty());
        personagens.forEach(personagem -> {
            assertTrue(personagem.getName().contains(partialName));
        });
    }

    @Test
    @DisplayName("Get Personagens Favoritos By Year of Birth/Ano de nascimento")
    void testGetListByPersonagemFavoritoYear() {
        int yearOfBirth = 1980;
        assertFalse(personagemService.getListByPersonagemFavoritoYear(yearOfBirth).isEmpty());
    }

    @Test
    public void testCreate() {
        Personagem personagem = new Personagem();
        personagemService.create(personagem);
        assertTrue(personagemService.getAllPersonagemFavorito().contains(personagem));
    }

    @Test
    @DisplayName("Delete Personagem Favorito. Verifica Diminuição do Tamanho da Lista")
    void testDelete() {
        Personagem personagem = new Personagem();
        personagemService.create(personagem);
        int tamanhoInicial = personagemService.getAllPersonagemFavorito().size();

        personagemService.delete(1);

        int tamanhoFinal = personagemService.getAllPersonagemFavorito().size();
        assertEquals(tamanhoInicial - 1, tamanhoFinal);
    }

    @Test
    @DisplayName("Update Personagem Favorito")
    void testUpdatePersonagemFavorito() {
        assumeTrue(personagemService.getAllPersonagemFavorito().size() >= 2, "Dados insuficiente.");

        int personagemIdParaAtualizar = 2;
        Personagem updatedPersonagem = new Personagem();
        updatedPersonagem.setName("Updated Personagem");

        personagemService.update(personagemIdParaAtualizar, updatedPersonagem);

        assertEquals(updatedPersonagem, personagemService.getByPersonagemFavoritoID(personagemIdParaAtualizar));
    }
}