package br.com.jhonatan.apontadorhorasapi.builders;

import java.time.LocalDateTime;

import br.com.jhonatan.apontadorhorasapi.dto.TimeDTO;

public class TimeDTOBuilder {

	private TimeDTO timeDTO;
	
	private TimeDTOBuilder() {}
	
	public static TimeDTOBuilder builder() {
		final TimeDTOBuilder timeDTOBuilder = new TimeDTOBuilder();
		timeDTOBuilder.timeDTO = new TimeDTO(null, LocalDateTime.now().minusHours(1L), LocalDateTime.now(), null);
		return timeDTOBuilder;
	}
	
	public TimeDTOBuilder withProjectId(Integer projectId) {
		timeDTO.setProjectId(projectId);
		return this;
	}
	
	public TimeDTO build() {
		return timeDTO;
	}
}
