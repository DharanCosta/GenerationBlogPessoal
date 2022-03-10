package org.blogpessoal.blogpessoal.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.blogpessoal.blogpessoal.model.Usuario;
import org.blogpessoal.blogpessoal.repository.UsuarioRepository;
import org.blogpessoal.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ResponseStatusException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	private @Autowired TestRestTemplate testRestTemplate;
	private @Autowired UsuarioService services;
	private @Autowired UsuarioRepository repository;
	
	
	@BeforeAll
	void start() {
		repository.deleteAll();
	}
	
	@Test
	@Order(1)
	@DisplayName("Teste Post  - Criar Usuario")	
	public void createUserReturn201() {
		// GIVEN
		HttpEntity<Usuario> request = new HttpEntity<Usuario>(
				new Usuario(0L,"Italo Boaz", "Italo77", "123456789"));
		// WHEN
		ResponseEntity<Usuario> response = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, request, Usuario.class);
		// THEN
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não permitir duplicação de Usuários")
	public void blockUserDuplication() {
		
		services.CadastrarUsuario(new Usuario(0L,"Dharan Boaz", "Dharan22", "123456789"));
		
		HttpEntity<Usuario> request = new HttpEntity<Usuario>(new Usuario(0L,"Dharan Boaz", "Dharan22", "123456789"));
		
		ResponseEntity<Usuario> response = testRestTemplate
				.exchange("/usuarios/cadastrar",HttpMethod.POST, request, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		
	}
	
	
	@Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void updateUsuario() {
		
		Optional<Usuario> usuarioCreate = services.CadastrarUsuario(new Usuario(0L,"Dharan BoLaz", "Dharan22", "123456789"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),"Dharan Costa", "Dharan22", "123456789");
		
		HttpEntity<Usuario> request = new HttpEntity<Usuario>(usuarioUpdate);
 		
		ResponseEntity<Usuario> response = testRestTemplate
				.withBasicAuth("Dharan","1234")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, request, Usuario.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(usuarioUpdate.getNome(),response.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(),response.getBody().getUsuario());
		
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void returnAllUsers() {
		
		services.CadastrarUsuario(new Usuario(0L,"Igor Boaz", "Igor55", "123456789"));
		
		services.CadastrarUsuario(new Usuario(0L,"Carlos Boaz", "Carlos23", "123456789"));
		
		ResponseEntity<String> response = testRestTemplate
				.withBasicAuth("Dharan", "1234")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
	}
}

