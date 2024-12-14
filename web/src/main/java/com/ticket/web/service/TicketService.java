package com.ticket.web.service;

import com.ticket.web.exception.ProcessedTicketException;
import com.ticket.web.exception.TicketNotFoundException;
import com.ticket.web.models.Ticket;
import com.ticket.web.repository.TicketRepository;
import com.ticket.web.repository.UsersRepository;
import com.ticket.web.models.Users;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    private UsersRepository usersRepository;

    //Employee Options
    public Ticket submitTicket(int amount, String description, String username) {
        Optional<Users> acc = usersRepository.findByUsername(username);

        if(acc.isPresent()){
            Ticket ticket =new Ticket(amount, description,username);
            return ticketRepository.save(ticket);
        }else{
            return null;
        }

    }

    public List<Ticket> getTicketsForUser(String username) {
        return ticketRepository.findByCreatedBy(username);
    }

    //Manager Options
    public List<Ticket> getPendingTickets() {
        return ticketRepository.findByStatus("Pending");
    }

    public Ticket processTicket(Long ticketId, String status) throws ProcessedTicketException,TicketNotFoundException{
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if(ticket.isPresent()){
            Ticket foundticket = ticket.get();
            if(foundticket.getStatus() != "Pending"){
                foundticket.setStatus(status);
                return ticketRepository.save(foundticket);
            }else{
                throw new ProcessedTicketException("Processed ticket");
            }
        }else{
            throw new TicketNotFoundException("Ticket not found");
        }
    }

}
