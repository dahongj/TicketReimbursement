package com.ticket.web;

import com.ticket.web.exception.AccountNotPresentException;
import com.ticket.web.exception.ProcessedTicketException;
import com.ticket.web.exception.TicketNotFoundException;
import com.ticket.web.models.Ticket;
import com.ticket.web.models.Users;
import com.ticket.web.service.*;
import com.ticket.web.repository.TicketRepository;
import com.ticket.web.repository.UsersRepository;

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

class TicketServiceTest {
    @InjectMocks
    private TicketService ticketService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitTicketSuccess() throws AccountNotPresentException {
        Users user = new Users(1, "user", "password123");
        when(usersRepository.findById(1)).thenReturn(Optional.of(user));
        Ticket ticket = new Ticket(100, "Test description", 1);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket result = ticketService.submitTicket(100, "Test description", 1);
        assertNotNull(result);
        assertEquals(100, result.getAmount());
    }

    @Test
    void testSubmitTicketUserNotFound() {
        when(usersRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AccountNotPresentException.class, () -> ticketService.submitTicket(100, "Test description", 1));
    }

    @Test
    void testGetTicketsForUser() {
        List<Ticket> tickets = Arrays.asList(new Ticket(100, "Description", 1));
        when(ticketRepository.findByCreatedBy(1)).thenReturn(tickets);

        List<Ticket> result = ticketService.getTicketsForUser(1);
        assertEquals(1, result.size());
        assertEquals("Description", result.get(0).getDescription());
    }

    @Test
    void testGetTicketsForUserEmpty() {
        when(ticketRepository.findByCreatedBy(1)).thenReturn(Arrays.asList());

        List<Ticket> result = ticketService.getTicketsForUser(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPendingTickets() {
        List<Ticket> tickets = Arrays.asList(new Ticket(100, "Description", 1));
        when(ticketRepository.findByStatus("Pending")).thenReturn(tickets);

        List<Ticket> result = ticketService.getPendingTickets();
        assertEquals(1, result.size());
    }

    @Test
    void testGetPendingTicketsEmpty() {
        when(ticketRepository.findByStatus("Pending")).thenReturn(Arrays.asList());

        List<Ticket> result = ticketService.getPendingTickets();
        assertTrue(result.isEmpty());
    }

}