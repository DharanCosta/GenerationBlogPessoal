package org.blogpessoal.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.blogpessoal.blogpessoal.model.Postagem;
import org.blogpessoal.blogpessoal.model.UserLogin;
import org.blogpessoal.blogpessoal.model.Usuario;
import org.blogpessoal.blogpessoal.repository.UsuarioRepository;
import org.blogpessoal.blogpessoal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@Tag(name="", description="")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository repository;
	
	//@Operation(summary= "Pegar todos usuários")
	/*@Apiresponses(value={
	 * @ApiResponse(
	 * 				responseCode ="200",
	 * 				description ="Resposta ok",
	 * 				content = {@Content(mediaType="aplication/json" schema =@schema(implementation = Usuarios.class))}
	 * ),
	 * @ApiResponse(
	 * 			responseCode= "404",
	 * 		description = "Usuário não encontrado",
	 * 		content = @Content
	 * ),
	 *  
	 * 
	 *} 
	 */
	
	@GetMapping("/all")
	public ResponseEntity <List<Usuario>> getAll() {
		return ResponseEntity.ok(repository.findAll());
	}
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable long id) {
		return repository.findById(id)
			.map(resp -> ResponseEntity.ok(resp))
			.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("username/{nome}")
	public ResponseEntity<List<Usuario>> GetByTitulo(@PathVariable String nome){
		return ResponseEntity.ok(repository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UserLogin> Autentication(@Valid @RequestBody Optional<UserLogin> user){
		return usuarioService.Logar(user)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> Post(@Valid @RequestBody Usuario usuario){
		return usuarioService.CadastrarUsuario(usuario)
				.map(resp -> ResponseEntity.status(HttpStatus.CREATED).body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario){		
		return usuarioService.atualizarUsuario(usuario)
			.map(resp -> ResponseEntity.status(HttpStatus.OK).body(resp))
			.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@ResponseStatus (HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id){
		Optional<Usuario> post = repository.findById(id);

		if(post.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		repository.deleteById(id);
	}

}
