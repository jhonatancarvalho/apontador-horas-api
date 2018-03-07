package br.com.jhonatan.apontadorhorasapi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jhonatan.apontadorhorasapi.domain.Project;
import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;
import br.com.jhonatan.apontadorhorasapi.repositories.ProjectRepository;
import br.com.jhonatan.apontadorhorasapi.services.exceptions.ObjectNotFoundException;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserService userService;
	
	public Project findById(Integer id) {
		return projectRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Project not found"));
	}
	
	public Project save(Project project) {
		return projectRepository.save(project);
	}
	
	public Project update(Project project, Integer id) {
		findById(id);
		project.setId(id);
		return save(project);
	}
	
	public List<Project> findAll() {
		return projectRepository.findAll();
	}

	public Project fromDTO(ProjectDTO newProjectDTO) {
		final Project project = new Project(null, newProjectDTO.getTitle(), newProjectDTO.getDescription());
		final List<User> users = new ArrayList<>();
		for (Integer userId : newProjectDTO.getUsersId()) {
			users.add(userService.findById(userId));
		}
		project.setUsers(users);
		return project;
	}
	
}
