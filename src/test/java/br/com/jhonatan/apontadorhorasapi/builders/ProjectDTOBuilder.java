package br.com.jhonatan.apontadorhorasapi.builders;

import java.util.List;

import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;

public class ProjectDTOBuilder {

	private ProjectDTO project;
	
	private ProjectDTOBuilder() {}
	
	public static ProjectDTOBuilder builder() {
		final ProjectDTOBuilder projectDTOBuilder = new ProjectDTOBuilder();
		projectDTOBuilder.project = new ProjectDTO("Project One", "First project of world.");
		return projectDTOBuilder;
	}
	
	public ProjectDTOBuilder withUsers(List<User> users) {
		for (User user : users) {
			project.getUsersId().add(user.getId());
		}
		return this;
	}

	public ProjectDTO build() {
		return project;
	}
	
}
