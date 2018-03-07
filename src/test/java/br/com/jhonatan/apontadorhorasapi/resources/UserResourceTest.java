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
     
    @Test
    public void deveBuscarUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("a@b.com.br").comLogin("ab").build();
       
        userRepository.save(user);
     
        mvc.perform(get("/user/" + user.getId())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id", is(user.getId())))
          .andExpect(jsonPath("$.name", is(user.getName())))
          .andExpect(jsonPath("$.email", is(user.getEmail())))
          .andExpect(jsonPath("$.login", is(user.getLogin())));
    }
    
    @Test
    public void naoDeveBuscarUserComIdNulo() throws Exception {

        mvc.perform(get("/user/" + null)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    }
    
    @Test
    public void naoDeveBuscarUserComIdInexistente() throws Exception {

        mvc.perform(get("/user/" + -1)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    }
    
    @Test
    public void deveSalvarUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("c@d.com.br").comLogin("cd").build();
        
        mvc.perform(MockMvcRequestBuilders.post("/user/")
          .content(objectMapper.writeValueAsString(user))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated());
    }
    
    @Test
    public void deveAlterarUser() throws Exception {
         
        final User user = UserBuilder.builder().comEmail("e@f.com.br").comLogin("ef").build();
        userRepository.save(user);
        user.setName("Roberta");
        
        mvc.perform(MockMvcRequestBuilders.put("/user/" + user.getId())
          .content(objectMapper.writeValueAsString(user))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());
        
        final User userSalvo = userRepository.findById(user.getId()).get();
        assertThat(userSalvo.getName(), is(user.getName()));
    }

}
