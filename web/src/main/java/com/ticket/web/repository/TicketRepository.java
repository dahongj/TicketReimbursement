package com.ticket.web.repository;

import com.ticket.web.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>{
    List<Ticket> findByCreatedBy(String username);
    List<Ticket> findByStatus(String status);
}
