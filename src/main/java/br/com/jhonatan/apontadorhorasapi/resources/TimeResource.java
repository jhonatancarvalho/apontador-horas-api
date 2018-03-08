package br.com.jhonatan.apontadorhorasapi.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.jhonatan.apontadorhorasapi.domain.Time;
import br.com.jhonatan.apontadorhorasapi.dto.TimeDTO;
import br.com.jhonatan.apontadorhorasapi.services.TimeService;

@RestController
@RequestMapping("/times")
public class TimeResource {

	@Autowired
	private TimeService timeService;
	
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody TimeDTO timeDTO) {
		final Time time = timeService.fromDTO(timeDTO);
		timeService.save(time);
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(time.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody TimeDTO timeDTO, @PathVariable Integer id) {
		final Time time = timeService.fromDTO(timeDTO);
		timeService.update(time, id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("{project_id}")
	public ResponseEntity<List<TimeDTO>> findByProjectId(@PathVariable(value="project_id") Integer projectId) {
		final List<TimeDTO> times = timeService.findByProjectId(projectId).stream().map(time -> new TimeDTO(time)).collect(Collectors.toList());
		return ResponseEntity.ok(times);
	}
	
}
