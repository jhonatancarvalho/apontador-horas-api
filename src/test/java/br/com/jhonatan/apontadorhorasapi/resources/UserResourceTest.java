package br.com.jhonatan.apontadorhorasapi.resources;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.jhonatan.apontadorhorasapi.builders.UserBuilder;
import br.com.jhonatan.apontadorhorasapi.domain.User;
import br.com.jhonatan.apontadorhorasapi.repositories.UserRepository;
import br.com.jhonatan.apontadorhorasapi.security.JWTUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(
  webEnvironment = WebEnvironment.RANDOM_PORT,
  classes = ApontadorHorasApiApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-test.properties")
public class UserResourceTest {

	@Autowired
    private MockMvc mvc;
 
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	private ObjectMapper objectMapper;
    
    @Autowired
	private JWTUtil jwtUtil;

    @Test
    public void shouldFindUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("a@b.com.br").comLogin("ab").build();
        userRepository.save(user);
     
        mvc.perform(get("/user/" + user.getId())    
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", is(user.getId())))
          .andExpect(jsonPath("$.name", is(user.getName())))
          .andExpect(jsonPath("$.email", is(user.getEmail())))
          .andExpect(jsonPath("$.login", is(user.getLogin())));
    }
    
    @Test
    public void notShouldFindUserWithIdNull() throws Exception {

    	final User user = UserBuilder.builder().comEmail("g@h.com.br").comLogin("gh").build();
    	userRepository.save(user);
    	
        mvc.perform(get("/user/" + null)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    }
    
    @Test
    public void notShouldFindUserWithIdNonExist() throws Exception {

    	final User user = UserBuilder.builder().comEmail("i@j.com.br").comLogin("ij").build();
    	userRepository.save(user);
    	
        mvc.perform(get("/user/" + -1)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldSaveUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("c@d.com.br").comLogin("cd").build();
        
        mvc.perform(MockMvcRequestBuilders.post("/user")
          .content(objectMapper.writeValueAsString(user))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
    
    @Test
    public void shouldUpdateUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("e@f.com.br").comLogin("ef").build();
        userRepository.save(user);
        user.setName("Roberta");
        
        mvc.perform(MockMvcRequestBuilders.put("/user/" + user.getId())
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(user.getLogin()))
          .content(objectMapper.writeValueAsString(user))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
        
        final User userSalvo = userRepository.findById(user.getId()).get();
        assertThat(userSalvo.getName(), is(user.getName()));
    }

}
