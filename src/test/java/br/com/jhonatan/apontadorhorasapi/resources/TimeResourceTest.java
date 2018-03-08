package br.com.jhonatan.apontadorhorasapi.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jhonatan.apontadorhorasapi.ApontadorHorasApiApplication;
import br.com.jhonatan.apontadorhorasapi.builders.ProjectDTOBuilder;
import br.com.jhonatan.apontadorhorasapi.builders.TimeDTOBuilder;
import br.com.jhonatan.apontadorhorasapi.builders.UserBuilder;
import br.com.jhonatan.apontadorhorasapi.domain.Project;
import br.com.jhonatan.apontadorhorasapi.domain.Time;
import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;
import br.com.jhonatan.apontadorhorasapi.dto.TimeDTO;
import br.com.jhonatan.apontadorhorasapi.repositories.ProjectRepository;
import br.com.jhonatan.apontadorhorasapi.repositories.TimeRepository;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.security.JWTUtil;
import br.com.jhonatan.apontadorhorasapi.services.ProjectService;
import br.com.jhonatan.apontadorhorasapi.services.TimeService;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = ApontadorHorasApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-test.properties")
public class TimeResourceTest {

	private static final String BASE_ENDPOINT_TIME = "/times";
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private TimeRepository timeRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TimeService timeService;
	
	@Autowired
	private ProjectService projectService;

    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
	private JWTUtil jwtUtil;
    
    @Test
    public void shouldFindTimeByProjectId() throws Exception {
         
        final User user = UserBuilder.builder().withEmail("time3@project.com").withLogin("time3").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
        
        final TimeDTO timeDTO = TimeDTOBuilder.builder().withProjectId(project.getId()).build();
        timeRepository.save(timeService.fromDTO(timeDTO));
     
        mvc.perform(get(BASE_ENDPOINT_TIME + "/" + project.getId())    
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
    
    @Test
    public void shouldSaveTime() throws Exception {
      
    	final User user = UserBuilder.builder().withEmail("time4@project.com").withLogin("time4").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
        
        final TimeDTO timeDTO = TimeDTOBuilder.builder().withProjectId(project.getId()).build();
        
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_TIME)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .content(objectMapper.writeValueAsString(timeDTO))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldSaveTimeWithId() throws Exception {
	  
    	final User user = UserBuilder.builder().withEmail("time99@project.com").withLogin("time99").build();
		userRepository.save(user);
	     
		final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
	   	final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
	     
	  	final TimeDTO timeDTO = TimeDTOBuilder.builder().withProjectId(project.getId()).build();
		final Time time = timeRepository.save(timeService.fromDTO(timeDTO));
		timeDTO.setId(time.getId());
		timeDTO.setEndedAt(null);
		timeDTO.setStartedAt(LocalDateTime.now().minusMinutes(5L));
        
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_TIME)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .content(objectMapper.writeValueAsString(timeDTO))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldUpdateTime() throws Exception {
         
    	final User user = UserBuilder.builder().withEmail("time5@project.com").withLogin("time5").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
        
        final ProjectDTO projectDTO2 = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project2 = projectRepository.save(projectService.fromDTO(projectDTO2));
        
        final TimeDTO timeDTO = TimeDTOBuilder.builder().withProjectId(project.getId()).build();
        final Time time = timeRepository.save(timeService.fromDTO(timeDTO));
        timeDTO.setProjectId(project2.getId());

        mvc.perform(MockMvcRequestBuilders.put(BASE_ENDPOINT_TIME + "/" + time.getId())
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .content(objectMapper.writeValueAsString(timeDTO))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
        
        final Time timeDb = timeRepository.findById(time.getId()).get();
        assertThat(timeDb.getProject(), is(project2));
    }
    
}
