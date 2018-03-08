package br.com.jhonatan.apontadorhorasapi.builders;

import java.util.List;

import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;

public class ProjectDTOBuilder {

	private ProjectDTO projectDTO;
	
	private ProjectDTOBuilder() {}
	
	public static ProjectDTOBuilder builder() {
		final ProjectDTOBuilder projectDTOBuilder = new ProjectDTOBuilder();
		projectDTOBuilder.projectDTO = new ProjectDTO(null, "Project One", "Prototype for world domination.");
		return projectDTOBuilder;
	}
	
	public ProjectDTOBuilder withUsers(List<User> users) {
		for (User user : users) {
			projectDTO.getUsersId().add(user.getId());
		}
		return this;
	}

	public ProjectDTO build() {
		return projectDTO;
	}
	
}
