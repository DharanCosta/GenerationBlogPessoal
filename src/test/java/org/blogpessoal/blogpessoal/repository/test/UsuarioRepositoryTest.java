package org.blogpessoal.blogpessoal.repository.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.blogpessoal.blogpessoal.model.Usuario;
import org.blogpessoal.blogpessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance (Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	private @Autowired UsuarioRepository repository;

	// GIVEN que eu tenho dharan@email no banco
	//WHEN eu pesquiso por dharan@email
	//THEN ele me retorna dharan@email
	
	@BeforeAll
	void start() {
		repository.deleteAll();
		repository.save(new Usuario(0L,"Dharan Costa","LinkFoto","dharan55","12345678","Administrador"));
		repository.save(new Usuario(0L,"Dhyana Costa","LinkFoto","dhyana66","12345678","Administrador"));
		repository.save(new Usuario(0L,"Orlando Costa","LinkFoto","Orlando77","12345678","Administrador"));
		repository.save(new Usuario(0L,"Cristiane Ristori","LinkFoto","Cristiane88","12345678","Administrador"));
	}
	
	@Test
	@DisplayName("Teste FindByUsuario")
	public void serchValidUsernanemReturnTrue() {
		
		Optional<Usuario> usuario = repository.findByUsuario("dharan55");
		assertTrue(usuario.get().getUsuario().equals("dharan55"));
	}
	
	@Test
	@DisplayName("Teste FindByName")
	public void searchThreeValidNames() {
		
		List<Usuario> listaDeUsuarios = repository.findAllByNomeContainingIgnoreCase("Costa");
		assertEquals(3,listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Dharan Costa"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Dhyana Costa"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Orlando Costa"));
		
	}
	
	
}
