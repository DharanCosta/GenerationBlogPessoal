package org.blogpessoal.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.blogpessoal.blogpessoal.model.UserLogin;
import org.blogpessoal.blogpessoal.model.Usuario;
import org.blogpessoal.blogpessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class service of operations from Usuario
 * 
 * @author Dharan
 * @since 07/03/2022
 * @version 1.0
 *
 */

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	/*  Cadastrar Usuário  */
	public Optional<Usuario> CadastrarUsuario(Usuario usuario) {
		Optional<Usuario> optional = repository.findByUsuario(usuario.getUsuario());
		if (optional.isPresent()) {
			return Optional.empty();
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String  senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		
		return Optional.ofNullable(repository.save(usuario));
	}
	
	/*  Logar Usuário  */
	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional <Usuario> usuario = repository.findByUsuario(user.get().getUsuario());

		if(usuario.isPresent())	{
			if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())){
				String auth = user.get().getUsuario() + ":" +user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader ="Basic " + new String(encodedAuth);
				
				user.get().setToken(authHeader);
				user.get().setId(usuario.get().getId());
				user.get().setNome(usuario.get().getNome());
				user.get().setFoto(usuario.get().getFoto());
				user.get().setTipo(usuario.get().getTipo());
				
				return user;
			}
		}
	return null;	
	}
	
	/*  Atualizar Usuário  */
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (repository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = repository.findByUsuario(usuario.getUsuario());

			if (buscaUsuario.isPresent()) {				
				if (buscaUsuario.get().getId() != usuario.getId())
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			}
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

			return Optional.of(repository.save(usuario));
		} 
			
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);		
	}	
	/* Criptografar Senha */
	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaEncoder = encoder.encode(senha);

		return senhaEncoder;
	}
	
	/* Comparar Senha 
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);
	}*/

	
	
}
