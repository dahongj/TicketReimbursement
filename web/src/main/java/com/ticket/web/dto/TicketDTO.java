package com.ticket.web.dto;

import com.ticket.web.models.Users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDTO {
    private Long id;
    private double amount;
    private String description;
    private String status; // "Pending", "Approved", "Denied"
    private Users createdBy;
}
