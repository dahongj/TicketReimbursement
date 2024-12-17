package com.ticket.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticket.web.models.*;
import com.ticket.web.exception.*;
import com.ticket.web.service.*;

import java.util.*;


@RestController
public class ReimburseController {
	private TicketService ticketService;
	private UserService userService;

	@Autowired
	public ReimburseController(TicketService ticketService, UserService userService){
		this.ticketService = ticketService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<Users> register(@RequestBody Users users) {
		try{
			Users newuser = userService.registerUser(users.getUsername(), users.getPassword());
			return ResponseEntity.ok(newuser);
		}catch(PreexistingUsernameException e){
			return ResponseEntity.status(409).body(null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Users> login(@RequestBody Users users) {
		try{
			Users loginuser = userService.loginUser(users.getUsername(), users.getPassword());
			return ResponseEntity.ok(loginuser);
		}catch(IncorrectLoginException e){
			return ResponseEntity.status(401).body(null);
		}
	}

	@PostMapping("/ticket/submit")
	public ResponseEntity<Ticket> submitTicket(@RequestBody Ticket ticket){
		try{
			Ticket newticket = ticketService.submitTicket(ticket.getAmount(), ticket.getDescription(), ticket.getCreatedBy());
			return ResponseEntity.ok(newticket);
		}catch(AccountNotPresentException e){
			return ResponseEntity.status(409).body(null);
		}
	}

	@GetMapping("/ticket/{userid}")
	public ResponseEntity<List<Ticket>> getTicketByUser(@PathVariable int userid){
		return ResponseEntity.status(200).body(ticketService.getTicketsForUser(userid));
	}

	@GetMapping("/ticket/pending")
	public ResponseEntity<List<Ticket>> getTicketPending(){
		return ResponseEntity.status(200).body(ticketService.getPendingTickets());
	}

	@PatchMapping("/ticket/process/{ticketid}")
	public ResponseEntity<Ticket> processTicket(@PathVariable Long ticketid,@RequestBody Ticket ticket){
		try{
			Ticket newticket = ticketService.processTicket(ticketid, ticket.getStatus());
			return ResponseEntity.status(200).body(newticket);
		}catch(ProcessedTicketException e){
			return ResponseEntity.status(409).body(null);
		}catch(TicketNotFoundException e){
			return ResponseEntity.status(409).body(null);
		}
	}
	

}