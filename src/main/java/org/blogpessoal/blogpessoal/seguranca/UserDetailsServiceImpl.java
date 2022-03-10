package org.blogpessoal.blogpessoal.seguranca;

import java.util.Optional;

import org.blogpessoal.blogpessoal.model.Usuario;
import org.blogpessoal.blogpessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) {
		Optional<Usuario> user = userRepository.findByUsuario(userName);
		//Mensagem de erro de não achar o usuário
		user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found"));
		// O retorno que eu procuro, o usuário
		return user.map(UserDetailsImpl::new).get();
	}
	
}
