package br.com.jhonatan.apontadorhorasapi.services.validation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jhonatan.apontadorhorasapi.dto.TimeDTO;
import br.com.jhonatan.apontadorhorasapi.services.validation.error.FieldMessage;

public class TimeInsertOrUpdateValidator implements ConstraintValidator<TimeInsertOrUpdate, TimeDTO> {

	@Autowired
	private HttpServletRequest request;

	@Override
	public boolean isValid(TimeDTO timeDTO, ConstraintValidatorContext context) {
		if (Objects.isNull(request)) {
			return true;
		}
		
		final List<FieldMessage> messages = new ArrayList<>();

		if (Objects.isNull(timeDTO.getProjectId())) {
			messages.add(new FieldMessage("projectId", "Required field"));
		}
		
		if (Objects.nonNull(timeDTO.getEndedAt()) && timeDTO.getEndedAt().isAfter(LocalDateTime.now())) {
			messages.add(new FieldMessage("endedAt", "Can not be after today's date"));
		}
		
		if (isUpdate()) {
			validateUpdate(timeDTO, messages);
		} else {
			validateInsert(timeDTO, messages);
		}

		for (FieldMessage fieldMessage : messages) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(fieldMessage.getMessage())
			.addPropertyNode(fieldMessage.getFieldName())
			.addConstraintViolation();
		}

		return messages.isEmpty();
	}

	private void validateInsert(TimeDTO timeDTO, final List<FieldMessage> messages) {
		if (Objects.nonNull(timeDTO.getStartedAt()) && Objects.nonNull(timeDTO.getEndedAt())) {
			if (timeDTO.getEndedAt().isBefore(timeDTO.getStartedAt())) {
				messages.add(new FieldMessage("endedAt", "End date can not be greater than start date"));
			}
			if (Objects.nonNull(timeDTO.getId())) {
				messages.add(new FieldMessage("id", "Should not be filled when dates are filled"));
			}
		}
		
		if (Objects.isNull(timeDTO.getStartedAt()) && Objects.isNull(timeDTO.getEndedAt())) {
			messages.add(new FieldMessage("startedAt", "One of the dates must be completed"));
		} else if ((Objects.isNull(timeDTO.getStartedAt()) || Objects.isNull(timeDTO.getEndedAt())) && Objects.isNull(timeDTO.getId())) {
			messages.add(new FieldMessage("id", "Required field"));
		}
	}
	
	private void validateUpdate(TimeDTO timeDTO, final List<FieldMessage> messages) {
		if (Objects.isNull(timeDTO.getStartedAt())) {
			messages.add(new FieldMessage("startedAt", "Required field"));
		}
		
		if (Objects.isNull(timeDTO.getEndedAt())) {
			messages.add(new FieldMessage("endedAt", "Required field"));
		}
		
		if (Objects.nonNull(timeDTO.getStartedAt()) && Objects.nonNull(timeDTO.getEndedAt()) && timeDTO.getEndedAt().isBefore(timeDTO.getStartedAt())) {
			messages.add(new FieldMessage("endedAt", "End date can not be greater than start date"));
		}
	}

	private boolean isUpdate() {
		return "PUT".equals(request.getMethod());
	}
}