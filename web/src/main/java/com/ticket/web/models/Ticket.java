package com.ticket.web.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Column(name = "ticketId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketid;
    private Integer amount;
    private String description;
    private String status;
    private Integer createdBy;

    public Ticket() {
    }

    public Ticket(Integer amount, String description, Integer username) {
        this.amount = amount;
        this.description = description;
        this.status = "Pending";
        this.createdBy = username;
    }

    public Long getId() {
        return ticketid;
    }

    public void setId(Long id) {
        this.ticketid = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ticket other = (Ticket) obj;
        if (ticketid == null) {
            if (other.ticketid != null)
                return false;
        } else if (!ticketid.equals(other.ticketid))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (amount != other.amount)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (createdBy == null) {
            if (other.createdBy != null)
                return false;
        } else if (!createdBy.equals(other.createdBy))
            return false;
        return true;
    }

    /**
     * Overriding the default toString() method allows for easy debugging.
     * 
     * @return a String representation of this class.
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketid +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdBy=" + createdBy +
                '}';
    }

}