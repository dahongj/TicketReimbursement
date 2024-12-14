package com.ticket.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticket.web.models.*;
import com.ticket.web.exception.*;
import com.ticket.web.service.*;

import java.lang.annotation.Repeatable;
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
	public ResponseEntity<Users> register(@RequestBody Users user) {
		try{
			Users newuser = userService.registerUser(user.getUsername(), user.getPassword());
			return ResponseEntity.ok(newuser);
		}catch(PreexistingUsernameException e){
			return ResponseEntity.status(409).body(null);
		}
	}

	@GetMapping("/login")
	public ResponseEntity<Users> login(@RequestBody Users user) {
		try{
			Users loginuser = userService.loginUser(user.getUsername(), user.getPassword());
			return ResponseEntity.ok(loginuser);
		}catch(IncorrectLoginException e){
			return ResponseEntity.status(409).body(null);
		}
	}

	

}