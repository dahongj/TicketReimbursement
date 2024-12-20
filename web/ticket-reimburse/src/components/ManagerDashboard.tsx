import React, { useState, useEffect } from "react";
import managerService from "../services/managerService";

interface Ticket {
  id: number;
  amount: number;
  description: string;
  status: string;
  createdBy: number;
}

const ManagerDashboard: React.FC = () => {
  const [pendingTickets, setPendingTickets] = useState<Ticket[]>([]);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    const fetchPendingTickets = async () => {
      try {
        const tickets = await managerService.getPendingTickets();
        setPendingTickets(tickets);
      } catch (err) {
        setError("Failed to fetch pending tickets.");
      }
    };

    fetchPendingTickets();
  }, []);

  const handleProcessTicket = async (ticket: Ticket, status: string) => {
    try {
      const updatedTicket = { ...ticket, status };
      await managerService.processTicket(updatedTicket);

      setPendingTickets((prevTickets) =>
        prevTickets.filter((t) => t.id !== ticket.id)
      );

      setError("");
    } catch (err) {
      setError(`Failed to process ticket with ID ${ticket.id}.`);
    }
  };

  return (
    <div style={containerStyle}>
      <table style={tableStyle}>
        <thead>
          <tr>
            <th style={headerCellStyle}>Amount</th>
            <th style={headerCellStyle}>Description</th>
            <th style={headerCellStyle}>Status</th>
            <th style={headerCellStyle}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {pendingTickets.map((ticket) => (
            <tr key={ticket.id}>
              <td style={cellStyle}>${ticket.amount}</td>
              <td style={cellStyle}>{ticket.description}</td>
              <td style={cellStyle}>{ticket.status}</td>
              <td style={cellStyle}>
                <button
                  onClick={() => handleProcessTicket(ticket, "Approved")}
                  style={actionButtonStyle("green")}
                >
                  Approve
                </button>
                <button
                  onClick={() => handleProcessTicket(ticket, "Denied")}
                  style={actionButtonStyle("red")}
                >
                  Deny
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

const containerStyle: React.CSSProperties = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  marginTop: "20px",
};

const titleStyle: React.CSSProperties = {
  marginBottom: "20px",
  fontSize: "24px",
  fontWeight: "bold",
};

const errorStyle: React.CSSProperties = {
  color: "red",
  marginBottom: "10px",
};

const tableStyle: React.CSSProperties = {
  borderCollapse: "collapse",
  width: "80%",
  margin: "0 auto",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
};

const headerCellStyle: React.CSSProperties = {
  border: "1px solid #ddd",
  padding: "8px",
  textAlign: "center",
  fontWeight: "bold",
  backgroundColor: "#f2f2f2",
};

const cellStyle: React.CSSProperties = {
  border: "1px solid #ddd",
  padding: "8px",
  textAlign: "center",
};

const noTicketsStyle: React.CSSProperties = {
  marginTop: "20px",
  fontSize: "16px",
  color: "#555",
};

const actionButtonStyle = (color: string): React.CSSProperties => ({
  padding: "8px 12px",
  margin: "0 5px",
  border: "none",
  borderRadius: "4px",
  backgroundColor: color,
  color: "white",
  cursor: "pointer",
  fontSize: "14px",
});

export default ManagerDashboard;
