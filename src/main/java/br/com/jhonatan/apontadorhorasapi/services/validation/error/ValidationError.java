package br.com.jhonatan.apontadorhorasapi.services.validation.error;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> messages = new ArrayList<>();

	public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getErros() {
		return messages;
	}

	public void addError(String fieldName, String message) {
		messages.add(new FieldMessage(fieldName, message));
	}
	
}
