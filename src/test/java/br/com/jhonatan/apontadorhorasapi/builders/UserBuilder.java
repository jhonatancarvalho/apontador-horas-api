package br.com.jhonatan.apontadorhorasapi.builders;

import br.com.jhonatan.apontadorhorasapi.domain.User;

public class UserBuilder {

	private User user;
	
	private UserBuilder() {}
	
	public static UserBuilder builder() {
		final UserBuilder userBuilder = new UserBuilder();
		userBuilder.user = new User(null, "Jhonatan Carvalho", "jhonatan.carvalho92@gmail.com", "jhonatan.carvalho", "123");
		return userBuilder;
	}
	
	public UserBuilder comEmail(String email) {
		user.setEmail(email);
		return this;
	}
	
	public UserBuilder comLogin(String login) {
		user.setLogin(login);
		return this;
	}
	
	public User build() {
		return user;
	}
	
}
