package com.ticket.web;

import com.ticket.web.models.Users;
import com.ticket.web.repository.UsersRepository;
import com.ticket.web.exception.*;
import com.ticket.web.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() throws PreexistingUsernameException {
        String username = "newuser";
        String password = "password123";

        when(usersRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users registeredUser = userService.registerUser(username, password);

        assertNotNull(registeredUser);
        assertEquals(username, registeredUser.getUsername());
        assertEquals(password, registeredUser.getPassword());
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    void registerUser_existingUsername() {
        String username = "existinguser";
        String password = "password123";

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(new Users(username, password)));

        assertThrows(PreexistingUsernameException.class, () -> userService.registerUser(username, password));
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void loginUser_success() throws IncorrectLoginException {
        String username = "user1";
        String password = "password123";

        Users mockUser = new Users(username, password);
        when(usersRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(mockUser));

        Users loggedInUser = userService.loginUser(username, password);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        verify(usersRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    void loginUser_incorrectCredentials() {
        String username = "user2";
        String password = "wrongpassword";

        when(usersRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        assertThrows(IncorrectLoginException.class, () -> userService.loginUser(username, password));
    }

    @Test
    void getAllUsers() {
        List<Users> mockUsers = Arrays.asList(
            new Users("user1", "password1"),
            new Users("user2", "password2")
        );

        when(usersRepository.findAll()).thenReturn(mockUsers);

        List<Users> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void editRole_success() throws AccountNotPresentException {
        Integer userId = 1;
        String newRole = "Manager";

        Users mockUser = new Users(userId, "user1", "password1");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users updatedUser = userService.editRole(userId, newRole);

        assertNotNull(updatedUser);
        assertEquals(newRole, updatedUser.getRole());
        verify(usersRepository, times(1)).save(mockUser);
    }

    @Test
    void editRole_userNotFound() {
        Integer userId = 2;
        String newRole = "Manager";

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(AccountNotPresentException.class, () -> userService.editRole(userId, newRole));
    }
}