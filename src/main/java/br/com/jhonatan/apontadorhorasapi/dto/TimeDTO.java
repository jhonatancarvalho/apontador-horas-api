package br.com.jhonatan.apontadorhorasapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.jhonatan.apontadorhorasapi.domain.Time;
import br.com.jhonatan.apontadorhorasapi.services.validation.TimeInsertOrUpdate;

@TimeInsertOrUpdate
public class TimeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("time_id")
	private Integer id;
	
	@JsonProperty("started_at")
	private LocalDateTime startedAt;
	
	@JsonProperty("ended_at")
	private LocalDateTime endedAt;
	
	@JsonProperty("project_id")
	private Integer projectId;

	public TimeDTO() {
		super();
	}
	
	public TimeDTO(Integer id, LocalDateTime startedAt, LocalDateTime endedAt, Integer projectId) {
		this.id = id;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.projectId = projectId;
	}

	public TimeDTO(Time time) {
		this.id = time.getId();
		this.startedAt = time.getStartedAt();
		this.endedAt = time.getEndedAt();
		this.projectId = time.getProject().getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(LocalDateTime endedAt) {
		this.endedAt = endedAt;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
}
