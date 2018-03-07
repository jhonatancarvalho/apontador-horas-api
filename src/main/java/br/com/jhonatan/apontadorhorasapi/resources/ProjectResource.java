package br.com.jhonatan.apontadorhorasapi.resources;

import java.net.URI;
import java.util.List;

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

import br.com.jhonatan.apontadorhorasapi.domain.Project;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;
import br.com.jhonatan.apontadorhorasapi.services.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectResource {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("{id}")
	public ResponseEntity<Project> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(projectService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<Void> save(@Valid @RequestBody ProjectDTO projectDTO) {
		final Project project = projectService.fromDTO(projectDTO);
		projectService.save(project);
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(project.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
		final Project project = projectService.fromDTO(projectDTO);
		projectService.update(project, id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	public ResponseEntity<List<Project>> findAll() {
		return ResponseEntity.ok(projectService.findAll());
	}
}
