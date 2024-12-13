package com.ticket.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDTO {
    private String username;
    private String role;
    private String password;
}
