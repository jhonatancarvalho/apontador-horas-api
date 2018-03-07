package br.com.jhonatan.apontadorhorasapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User findById(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("User not found"));
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public User update(User user, Integer id) {
		findById(id);
		user.setId(id);
		return save(user);
	}
	
}
