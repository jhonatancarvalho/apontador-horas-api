package br.com.jhonatan.apontadorhorasapi.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.jhonatan.apontadorhorasapi.builders.UserBuilder;
import br.com.jhonatan.apontadorhorasapi.domain.Project;
import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.dto.ProjectDTO;
import br.com.jhonatan.apontadorhorasapi.repositories.ProjectRepository;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.security.JWTUtil;
import br.com.jhonatan.apontadorhorasapi.services.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = ApontadorHorasApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-test.properties")
public class ProjectResourceTest {

	private static final String BASE_ENDPOINT_PROJECT = "/projects";
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProjectService projectService;

    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
	private JWTUtil jwtUtil;
    
    @Test
    public void shouldFindProject() throws Exception {
         
        final User user = UserBuilder.builder().withEmail("project3@project.com").withLogin("project3").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
     
        mvc.perform(get(BASE_ENDPOINT_PROJECT + "/" + project.getId())    
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", is(project.getId())))
          .andExpect(jsonPath("$.title", is(project.getTitle())))
          .andExpect(jsonPath("$.description", is(project.getDescription())))
          .andExpect(jsonPath("$.user_id", is(projectDTO.getUsersId())));
    }
    
    @Test
    public void shouldSaveProject() throws Exception {
      
        final User user1 = UserBuilder.builder().withEmail("userProject1@project.com").withLogin("userProject1").build();
        final User user2 = UserBuilder.builder().withEmail("userProject2@project.com").withLogin("userProject2").build();
        userRepository.saveAll(Arrays.asList(user1, user2));
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user1, user2)).build();
        
        mvc.perform(MockMvcRequestBuilders.post(BASE_ENDPOINT_PROJECT)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user1.getLogin()))
          .content(objectMapper.writeValueAsString(projectDTO))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldUpdateProject() throws Exception {
         
    	final User user = UserBuilder.builder().withEmail("userProject8@project.com").withLogin("userProject8").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final Project project = projectRepository.save(projectService.fromDTO(projectDTO));
        projectDTO.setTitle("novoTitle");

        mvc.perform(MockMvcRequestBuilders.put(BASE_ENDPOINT_PROJECT + "/" + project.getId())
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .content(objectMapper.writeValueAsString(projectDTO))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
        
        final Project projectDb = projectRepository.findById(project.getId()).get();
        assertThat(projectDb.getTitle(), is(projectDTO.getTitle()));
    }
    
    @Test
    public void shouldFindAllProject() throws Exception {
         
    	final User user = UserBuilder.builder().withEmail("userProject9@project.com").withLogin("userProject9").build();
        userRepository.save(user);
        
        final ProjectDTO projectDTO1 = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        final ProjectDTO projectDTO2 = ProjectDTOBuilder.builder().withUsers(Arrays.asList(user)).build();
        projectRepository.saveAll(Arrays.asList(projectService.fromDTO(projectDTO1), projectService.fromDTO(projectDTO2)));
        final Integer sizeProjects = (int) projectRepository.count();

        mvc.perform(MockMvcRequestBuilders.get(BASE_ENDPOINT_PROJECT)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(sizeProjects)));
    }
    
}
