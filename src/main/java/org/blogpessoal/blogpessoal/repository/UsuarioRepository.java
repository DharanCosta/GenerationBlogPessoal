package org.blogpessoal.blogpessoal.repository;

import java.util.Optional;

import org.blogpessoal.blogpessoal.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long >{
		
	public Optional<Usuario> findByUsuario (String usuario);
	
}
