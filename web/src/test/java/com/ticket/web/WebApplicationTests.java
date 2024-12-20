package com.ticket.web;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.ticket.web.controller.ReimburseController;
import com.ticket.web.models.Ticket;
import com.ticket.web.models.Users;
import com.ticket.web.repository.TicketRepository;
import com.ticket.web.repository.UsersRepository;
import com.ticket.web.service.TicketService;
import com.ticket.web.service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class WebApplicationTests {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ReimburseController reimburseController;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(reimburseController, "ReimburseController should not be null.");
        Assertions.assertNotNull(ticketService, "TicketService should not be null.");
        Assertions.assertNotNull(userService, "UserService should not be null.");
        Assertions.assertNotNull(ticketRepository, "TicketRepository should not be null.");
        Assertions.assertNotNull(usersRepository, "UsersRepository should not be null.");
    }

    @Test
    public void testMainMethod() {
        // Ensures that the main method does not throw any exceptions
        assertDoesNotThrow(() -> {
            String[] args = {};
            WebApplication.main(args);
        });
    }

}
