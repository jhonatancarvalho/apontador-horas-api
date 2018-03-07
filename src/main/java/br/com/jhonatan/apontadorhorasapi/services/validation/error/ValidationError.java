package br.com.jhonatan.apontadorhorasapi.services.validation.error;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<FieldMessage> listaMensagens = new ArrayList<>();

	public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
		super(timestamp, status, error, message, path);
	}

	public List<FieldMessage> getErros() {
		return listaMensagens;
	}

	public void addError(String fieldName, String message) {
		listaMensagens.add(new FieldMessage(fieldName, message));
	}
	
}
