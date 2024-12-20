package com.ticket.web;

import com.ticket.web.models.Users;
import com.ticket.web.service.TicketService;
import com.ticket.web.service.UserService;
import com.ticket.web.controller.ReimburseController;
import com.ticket.web.exception.IncorrectLoginException;
import com.ticket.web.exception.PreexistingUsernameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserTest {
    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReimburseController reimburseController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reimburseController).build();
    }
    
    @Test
void testUserConstructorAndGetters() {
    Users user = new Users(1, "user", "password123");
    
    assertThat(user.getAccountId()).isEqualTo(1);
    assertThat(user.getUsername()).isEqualTo("user");
    assertThat(user.getPassword()).isEqualTo("password123");
    assertThat(user.getRole()).isEqualTo("Employee"); 
}

@Test
void testUserConstructorWithNoId() {
    Users user = new Users("user", "securepassword");
    
    assertThat(user.getUsername()).isEqualTo("user");
    assertThat(user.getPassword()).isEqualTo("securepassword");
    assertThat(user.getRole()).isEqualTo("Employee"); 
}

@Test
void testSetters() {
    Users user = new Users("oldusername", "oldpassword");

    user.setAccountId(2);
    user.setUsername("newusername");
    user.setPassword("newpassword");
    user.setRole("Manager");

    assertThat(user.getAccountId()).isEqualTo(2);
    assertThat(user.getUsername()).isEqualTo("newusername");
    assertThat(user.getPassword()).isEqualTo("newpassword");
    assertThat(user.getRole()).isEqualTo("Manager"); 
}

@Test
void testEqualsMethod() {
    Users user1 = new Users(1, "user", "password123");
    Users user2 = new Users(1, "user", "password123");
    assertThat(user1).isEqualTo(user2);
    user2.setPassword("newpassword");
    assertThat(user1).isNotEqualTo(user2);
    assertThat(user1).isNotEqualTo(null);
    assertThat(user1).isNotEqualTo("string");
}

@Test
void testToStringMethod() {
    Users user = new Users(1, "user", "password123");
    
    String expectedString = "Users{accountId=1, username='user', password='password123'}";
    assertThat(user.toString()).isEqualTo(expectedString);
    user.setUsername("user");
    user.setPassword("newpassword123");
    expectedString = "Users{accountId=1, username='user', password='newpassword123'}";
    assertThat(user.toString()).isEqualTo(expectedString);
}

@Test
void testEqualsWithSameObject() {
    Users user = new Users(1, "user", "password123");
    assertThat(user).isEqualTo(user);
}

@Test
void testEqualsWithDifferentObjectType() {
    Users user = new Users(1, "user", "password123");
    String str = "Not a user object";
    assertThat(user).isNotEqualTo(str);
}

@Test
    public void testRegisterSuccess() throws Exception {
        Users user = new Users("user", "password123");
        when(userService.registerUser("user", "password123")).thenReturn(user);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void testRegisterFailure() throws Exception {
        when(userService.registerUser("user", "password123")).thenThrow(new PreexistingUsernameException("Username exists"));

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        Users user = new Users("user", "password123");
        when(userService.loginUser("user", "password123")).thenReturn(user);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(userService.loginUser("user", "password123")).thenThrow(new IncorrectLoginException("Incorrect login credentials"));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"user\", \"password\":\"password123\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUsers() throws Exception {
        Users user = new Users(1, "user", "password123");
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/manager/allusers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user"));
    }

    @Test
    public void testEditRole() throws Exception {
        Users user = new Users(1, "user", "password123");
        user.setRole("Manager");
 
        when(userService.editRole(1, "Manager")).thenReturn(user);
    
        mockMvc.perform(patch("/manager/editrole/{userid}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"role\":\"Manager\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("Manager"));
    }
}

