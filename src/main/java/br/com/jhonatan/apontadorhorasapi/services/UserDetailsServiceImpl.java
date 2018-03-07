package br.com.jhonatan.apontadorhorasapi.services;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.security.UserSystem;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		final User user = userRepository.findByLogin(login);
		if (Objects.isNull(user)) {
			throw new UsernameNotFoundException(login);
		}

		return new UserSystem(user.getId(), user.getLogin(), user.getPassword());
	}

}
