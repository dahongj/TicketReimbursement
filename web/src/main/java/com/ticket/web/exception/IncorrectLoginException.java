package com.ticket.web.exception;

public class IncorrectLoginException extends Exception{
    public IncorrectLoginException(String errormessage){
        super(errormessage);
    }
}
