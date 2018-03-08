package br.com.jhonatan.apontadorhorasapi.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.services.validation.error.FieldMessage;

public class UserInsertOrUpdateValidator implements ConstraintValidator<UserInsertOrUpdate, User> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {
		if (Objects.isNull(request)) {
			return true;
		}
		
		final List<FieldMessage> messages = new ArrayList<>();

		if (isUpdate()) {
			validateUpdate(user, messages);
		} else {
			validateInsert(user, messages);
		}

		for (FieldMessage fieldMessage : messages) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fieldMessage.getMessage())
			.addPropertyNode(fieldMessage.getFieldName())
			.addConstraintViolation();
		}

		return messages.isEmpty();
	}

	private void validateInsert(User user, final List<FieldMessage> messages) {
		User userDb = userRepository.findByLogin(user.getLogin());
		if (Objects.nonNull(userDb)) {
			messages.add(new FieldMessage("login", "Login already exists"));
		}

		userDb = userRepository.findByEmail(user.getEmail());
		if (Objects.nonNull(userDb)) {
			messages.add(new FieldMessage("email", "Email already exists"));
		}
	}
	
	private void validateUpdate(User user, final List<FieldMessage> messages) {
		
		@SuppressWarnings("unchecked")
		final Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);	
		final Integer uriId = Integer.valueOf(map.get("id"));
		
		User userDb = userRepository.findByLogin(user.getLogin());
		if (Objects.nonNull(userDb) && userDb.getId().compareTo(uriId) != 0) {
			messages.add(new FieldMessage("login", "Login already exists"));
		}

		userDb = userRepository.findByEmail(user.getEmail());
		if (Objects.nonNull(userDb) && userDb.getId().compareTo(uriId) != 0) {
			messages.add(new FieldMessage("email", "Email already exists"));
		}
	}

	private boolean isUpdate() {
		return "PUT".equals(request.getMethod());
	}
}