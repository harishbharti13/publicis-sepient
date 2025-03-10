package com.publicis.orchestration_backend;

import com.publicis.orchestration_backend.controller.UserController;
import com.publicis.orchestration_backend.entity.User;
import com.publicis.orchestration_backend.exception.UserNotFoundException;
import com.publicis.orchestration_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testFindUserByEmail_Success() throws Exception {
        User user = new User();
        user.setEmail("test@publicis.com");
        Mockito.when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/users/email/test@publicis.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@publicis.com"));
    }

    @Test
    void testFindUserByEmail_NotFound() throws Exception {
        Mockito.when(userService.findByEmail(anyString()))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/email/test@publicis.com"))
                .andExpect(status().isNotFound()) // Expect 404 now
                .andExpect(jsonPath("$.error").value("User Not Found"))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testFindUserById_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        Mockito.when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testFindUserById_NotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenThrow(new UserNotFoundException("User with ID 1 not found"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User Not Found"))
                .andExpect(jsonPath("$.message").value("User with ID 1 not found"));
    }

    @Test
    void testSearchUsers_Success() throws Exception {
        User user1 = new User(); user1.setFirstName("John");
        User user2 = new User(); user2.setFirstName("Jane");

        List<User> users = List.of(user1, user2);
        Mockito.when(userService.searchUsers("John")).thenReturn(users);

        mockMvc.perform(get("/users/search").param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testLoadUsersFromExternalAPI() throws Exception {
        Mockito.doNothing().when(userService).loadUsersFromExternalAPI();

        mockMvc.perform(post("/users/load"))
                .andExpect(status().isOk())
                .andExpect(content().string("Users loaded successfully"));

        Mockito.verify(userService, Mockito.times(1)).loadUsersFromExternalAPI();
    }


}