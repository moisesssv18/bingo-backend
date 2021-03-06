package br.com.bingo.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bingo.models.SecUsuarios;
import br.com.bingo.repository.sec_usuariosRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	sec_usuariosRepository sec_usuariosRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String LOGINUSUARIO) throws UsernameNotFoundException {
		SecUsuarios usuario = sec_usuariosRepository.findByLOGINUSUARIO(LOGINUSUARIO);
				//.orElseThrow(() -> new UsernameNotFoundException("Usuario não foi encontrado com o EMAIL: " + EMAIL));
		return UserDetailsImpl.build(usuario);
	}

}
