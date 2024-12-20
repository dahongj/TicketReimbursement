import React, { useEffect, useState } from "react";
import employeeService from "../services/employeeService";

interface Ticket {
  id: number;
  amount: number;
  description: string;
  status: string;
  createdBy: number;
}

interface ViewTicketsPageProps {
  accountId: number;
}

const ViewTicketsPage: React.FC<ViewTicketsPageProps> = ({ accountId }) => {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    const fetchTickets = async () => {
      try {
        const fetchedTickets = await employeeService.getTicketsByEmployee(
          accountId
        );
        setTickets(fetchedTickets);
      } catch (err) {
        setError("Failed to fetch tickets. Please try again later.");
      }
    };

    fetchTickets();
  }, [accountId]);

  return (
    <div style={containerStyle}>
      <h2 style={titleStyle}>Your Tickets</h2>
      {error && <p style={errorStyle}>{error}</p>}
      {tickets.length > 0 ? (
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={headerCellStyle}>Amount</th>
              <th style={headerCellStyle}>Description</th>
              <th style={headerCellStyle}>Status</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((ticket) => (
              <tr key={ticket.id}>
                <td style={cellStyle}>{ticket.amount}</td>
                <td style={cellStyle}>{ticket.description}</td>
                <td style={cellStyle}>{ticket.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p style={noTicketsStyle}>No tickets found.</p>
      )}
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

export default ViewTicketsPage;
