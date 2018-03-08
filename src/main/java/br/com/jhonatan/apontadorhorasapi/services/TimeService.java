package br.com.jhonatan.apontadorhorasapi.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jhonatan.apontadorhorasapi.domain.Project;
import br.com.jhonatan.apontadorhorasapi.domain.Time;
import br.com.jhonatan.apontadorhorasapi.dto.TimeDTO;
import br.com.jhonatan.apontadorhorasapi.repositories.TimeRepository;
import br.com.jhonatan.apontadorhorasapi.services.exceptions.ObjectNotFoundException;

@Service
public class TimeService {

	@Autowired
	private TimeRepository timeRepository;
	
	@Autowired
	private ProjectService projectService;
	
	public Time save(Time time) {
		if (Objects.nonNull(time.getId())) {
			return updateInSave(time);
		}
		return timeRepository.save(time);
	}

	public Time update(Time time, Integer id) {
		time.setId(id);
		return timeRepository.save(time);
	}
	
	public List<Time> findByProjectId(Integer projectId) {
		final Project project = projectService.findById(projectId);
		return project.getTimes();
	}
	
	private Time updateInSave(Time time) {
		final Time timeDb = timeRepository.findById(time.getId())
				.orElseThrow(() -> new ObjectNotFoundException("Time not found"));
		if (Objects.nonNull(time.getStartedAt())) {
			timeDb.setStartedAt(time.getStartedAt());
		}
		if (Objects.nonNull(time.getEndedAt())) {
			timeDb.setEndedAt(time.getEndedAt());
		}
		return timeRepository.save(timeDb);
	}

	public Time fromDTO(TimeDTO timeDTO) {
		return new Time(timeDTO.getId(), timeDTO.getStartedAt(), timeDTO.getEndedAt(), projectService.findById(timeDTO.getProjectId()));
	}
	
}
