package com.ticket.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticket.web.models.*;
import com.ticket.web.exception.*;
import com.ticket.web.service.*;

import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000") 
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
			System.out.println("Registering User");
			return ResponseEntity.ok(newuser);
		}catch(PreexistingUsernameException e){
			System.out.println("Username exists");
			return ResponseEntity.status(409).body(null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Users> login(@RequestBody Users users) {
		try{
			Users loginuser = userService.loginUser(users.getUsername(), users.getPassword());
			System.out.println("Logging in");
			return ResponseEntity.ok(loginuser);
		}catch(IncorrectLoginException e){
			System.out.println("Incorrect login credentials");
			return ResponseEntity.status(401).body(null);
		}
	}

	@PostMapping("/ticket/submit")
	public ResponseEntity<Ticket> submitTicket(@RequestBody Ticket ticket){
		try{
			Ticket newticket = ticketService.submitTicket(ticket.getAmount(), ticket.getDescription(), ticket.getCreatedBy());
			System.out.println("Submitting new ticket");
			return ResponseEntity.ok(newticket);
		}catch(AccountNotPresentException e){
			System.out.println("Account does not exist");
			return ResponseEntity.status(409).body(null);
		}
	}

	@GetMapping("/ticket/{userid}")
	public ResponseEntity<List<Ticket>> getTicketByUser(@PathVariable int userid){
		System.out.println("Getting ticket for user with userid: " + userid);
		return ResponseEntity.status(200).body(ticketService.getTicketsForUser(userid));
	}

	@GetMapping("/ticket/pending")
	public ResponseEntity<List<Ticket>> getTicketPending(){
		System.out.println("Getting pending ticket");
		return ResponseEntity.status(200).body(ticketService.getPendingTickets());
	}

	@PatchMapping("/ticket/process/{ticketid}")
	public ResponseEntity<Ticket> processTicket(@PathVariable Long ticketid,@RequestBody Ticket ticket){
		try{
			Ticket newticket = ticketService.processTicket(ticketid, ticket.getStatus());
			System.out.println("Processing ticket with id: " + ticketid);
			return ResponseEntity.status(200).body(newticket);
		}catch(ProcessedTicketException e){
			System.out.println("Ticket is already processed");
			return ResponseEntity.status(409).body(null);
		}catch(TicketNotFoundException e){
			System.out.println("Ticket is not found");
			return ResponseEntity.status(409).body(null);
		}
	}

	@GetMapping("/manager/allusers")
	public ResponseEntity<List<Users>> getUsers(){
		System.out.println("Getting all users");
		return ResponseEntity.status(200).body(userService.getAllUsers());
	}
	
	@PatchMapping("/manager/editrole/{userid}")
	public ResponseEntity<Users> editRole(@PathVariable Integer userid,@RequestBody Users user){
		try{
			Users editUser = userService.editRole(userid, user.getRole());
			System.out.println("Editing role for user: " + userid);
			return ResponseEntity.status(200).body(editUser);
		}catch(AccountNotPresentException e){
			System.out.println("Account is not present");
			return ResponseEntity.status(409).body(null);
		}
	}

}