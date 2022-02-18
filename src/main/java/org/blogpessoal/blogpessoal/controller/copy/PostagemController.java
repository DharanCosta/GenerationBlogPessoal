package org.blogpessoal.blogpessoal.controller.copy;

import java.util.List;

import org.blogpessoal.blogpessoal.model.Postagem;
import org.blogpessoal.blogpessoal.repository.PostagemRepository;
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
import org.springframework.web.bind.annotation.RestController;

// Configurações do Controller para receber requisições \/
@RestController 
@RequestMapping("/postagens") 
@CrossOrigin("*")

public class PostagemController {

	@Autowired  // Autowired é a gestão de dependencias do Spring
	private PostagemRepository repositoty;
				
	//Método Get-FindAll em formato de lista
	@GetMapping  // Requisão de um método Get
	public ResponseEntity<List<Postagem>> GetAll(){
		return ResponseEntity.ok(repositoty.findAll());
	}
	
	//Método Get-FindByID 
	@GetMapping ("/{id}")
	public ResponseEntity<Postagem> GetById(@PathVariable long id){
		return repositoty.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	//Método Get-FindByTítulo
	@GetMapping ("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> GetByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(repositoty.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//Método Post 
	@PostMapping
	public ResponseEntity<Postagem> post (@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(repositoty.save(postagem));
	}
	
	//Método Put
	@PutMapping
	public ResponseEntity<Postagem> put (@RequestBody Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repositoty.save(postagem));
	}
	
	//Método Delete
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id){
		repositoty.deleteById(id);
	}
}
