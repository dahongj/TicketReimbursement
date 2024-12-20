package com.ticket.web;

import com.ticket.web.models.Ticket;
import com.ticket.web.service.TicketService;
import com.ticket.web.service.UserService;
import com.ticket.web.controller.ReimburseController;
import com.ticket.web.exception.AccountNotPresentException;
import com.ticket.web.exception.ProcessedTicketException;
import com.ticket.web.exception.TicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class TicketTest {
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
    void testTicketConstructorAndGetters() {
        Ticket ticket = new Ticket(100, "Travel expenses", 1);

        assertThat(ticket.getAmount()).isEqualTo(100);
        assertThat(ticket.getDescription()).isEqualTo("Travel expenses");
        assertThat(ticket.getCreatedBy()).isEqualTo(1);
        assertThat(ticket.getStatus()).isEqualTo("Pending");
    }

    @Test
    void testSetters() {
        Ticket ticket = new Ticket();
        ticket.setAmount(200);
        ticket.setDescription("Office supplies");
        ticket.setCreatedBy(2);
        ticket.setStatus("Approved");
        ticket.setId(10L);

        assertThat(ticket.getAmount()).isEqualTo(200);
        assertThat(ticket.getDescription()).isEqualTo("Office supplies");
        assertThat(ticket.getCreatedBy()).isEqualTo(2);
        assertThat(ticket.getStatus()).isEqualTo("Approved");
        assertThat(ticket.getId()).isEqualTo(10L);
    }

    @Test
    void testEquals() {
        Ticket ticket1 = new Ticket(100, "Travel expenses", 1);
        ticket1.setId(1L);

        Ticket ticket2 = new Ticket(200, "Office supplies", 2);
        ticket2.setId(1L);

        Ticket ticket3 = new Ticket(300, "Hotel stay", 3);
        ticket3.setId(2L);
        assertThat(ticket1).isNotEqualTo(ticket2);
        assertThat(ticket1).isNotEqualTo(ticket3);
    }

    @Test
    void testToString() {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        ticket.setId(5L);
        String expectedString = "Ticket{ticketId=5, amount=150, description='Conference fees', status=Pending, createdBy=3}";
        assertThat(ticket.toString()).isEqualTo(expectedString);
    }

    @Test
    void testTicketConstructorWithDefaultStatus() {
        Ticket ticket = new Ticket(100, "Office Supplies", 2);
        assertThat(ticket.getStatus()).isEqualTo("Pending");
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        Ticket ticket = new Ticket(100, "Office Supplies", 2);
        assertThat(ticket).isNotEqualTo(null);
        String str = "Fake ticket";
        assertThat(ticket).isNotEqualTo(str);
    }

    @Test
    public void testSubmitTicketSuccess() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        when(ticketService.submitTicket(150, "Conference fees", 3)).thenReturn(ticket);

        mockMvc.perform(post("/ticket/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":150, \"description\":\"Conference fees\", \"createdBy\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Conference fees"));
    }

    @Test
    public void testSubmitTicketFailure() throws Exception {
        when(ticketService.submitTicket(150, "Conference fees", 3))
                .thenThrow(new AccountNotPresentException("Account does not exist"));

        mockMvc.perform(post("/ticket/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":150, \"description\":\"Conference fees\", \"createdBy\":3}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetTicketByUser() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        when(ticketService.getTicketsForUser(3)).thenReturn(List.of(ticket));

        mockMvc.perform(get("/ticket/{userid}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Conference fees"));
    }

    @Test
    public void testGetTicketPending() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        when(ticketService.getPendingTickets()).thenReturn(List.of(ticket));

        mockMvc.perform(get("/ticket/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Conference fees"));
    }

    @Test
    public void testProcessTicketSuccess() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        ticket.setStatus("Pending");

        Ticket processedTicket = new Ticket(150, "Conference fees", 3);
        processedTicket.setStatus("Approved");

        when(ticketService.processTicket(anyLong(), eq("Approved"))).thenReturn(processedTicket);

        mockMvc.perform(patch("/ticket/process/{ticketid}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\": \"Approved\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Approved"));
    }

    @Test
    public void testProcessTicketFailure_AlreadyProcessed() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        ticket.setStatus("Approved");

        when(ticketService.processTicket(anyLong(), eq("Approved")))
                .thenThrow(new ProcessedTicketException("Ticket already processed"));

        mockMvc.perform(patch("/ticket/process/{ticketid}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\": \"Approved\" }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").doesNotExist());
    }

    @Test
    public void testProcessTicketFailure_TicketNotFound() throws Exception {
        Ticket ticket = new Ticket(150, "Conference fees", 3);
        ticket.setStatus("Pending");

        when(ticketService.processTicket(anyLong(), eq("Approved")))
                .thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(patch("/ticket/process/{ticketid}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"status\": \"Approved\" }"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").doesNotExist());
    }

}
