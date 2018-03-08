package br.com.jhonatan.apontadorhorasapi.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.jhonatan.apontadorhorasapi.domain.Project;

public class ProjectDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotEmpty(message="Required field")
	private String title;
	
	private String description;

	@JsonProperty("user_id")
	@NotEmpty(message="Required field")
	private List<Integer> usersId = new ArrayList<>();
	
	public ProjectDTO() {
		super();
	}

	public ProjectDTO(Integer id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public ProjectDTO(Project project) {
		this.id = project.getId();
		this.title = project.getTitle();
		this.description = project.getDescription();
		project.getUsers().forEach(user -> {
			this.usersId.add(user.getId());
		});
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Integer> getUsersId() {
		return usersId;
	}

	public void setUsersId(List<Integer> usersId) {
		this.usersId = usersId;
	}

}
