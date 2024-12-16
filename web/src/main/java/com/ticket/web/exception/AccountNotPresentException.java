package com.ticket.web.exception;


public class AccountNotPresentException extends Exception{
    public AccountNotPresentException(String errormessage){
        super(errormessage);
    }
}
