package com.ticket.web.exception;

public class PreexistingUsernameException extends Exception{
    public PreexistingUsernameException(String message){
        super(message);
    }
}
