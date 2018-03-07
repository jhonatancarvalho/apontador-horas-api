package br.com.jhonatan.apontadorhorasapi.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Required field")
	private String title;
	
	private String description;

	@JsonProperty("user_id")
	@NotEmpty(message="Required field")
	private List<Integer> usersId = new ArrayList<>();
	
	public ProjectDTO() {
		super();
	}

	public ProjectDTO(String title, String description) {
		this.title = title;
		this.description = description;
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
