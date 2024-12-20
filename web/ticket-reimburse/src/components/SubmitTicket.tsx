import React, { useState } from "react";
import employeeService from "../services/employeeService";

interface SubmitTicketPageProps {
  accountId: number;
}

const SubmitTicketPage: React.FC<SubmitTicketPageProps> = ({ accountId }) => {
  const [amount, setAmount] = useState<number>(0);
  const [description, setDescription] = useState<string>("");
  const [error, setError] = useState<string>("");
  const [success, setSuccess] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (amount <= 0 || !description.trim()) {
      setError(
        "Amount must be greater than 0 and description cannot be empty."
      );
      return;
    }

    const ticket = {
      amount,
      description,
      createdBy: accountId,
    };

    try {
      await employeeService.submitTicket(ticket);
      setSuccess("Ticket submitted successfully!");
      setAmount(0);
      setDescription("");
      setError("");
    } catch (err) {
      setError("Failed to submit ticket. Please try again later.");
      setSuccess("");
    }
  };

  return (
    <div style={containerStyle}>
      <form onSubmit={handleSubmit} style={formStyle}>
        <h2 style={titleStyle}>Submit a Ticket</h2>
        {error && <p style={errorStyle}>{error}</p>}
        {success && <p style={successStyle}>{success}</p>}
        <div style={inputGroupStyle}>
          <label htmlFor="amount" style={labelStyle}>
            Amount:
          </label>
          <input
            id="amount"
            type="number"
            value={amount}
            onChange={(e) => setAmount(parseFloat(e.target.value))}
            style={inputStyle}
          />
        </div>
        <div style={inputGroupStyle}>
          <label htmlFor="description" style={labelStyle}>
            Description:
          </label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            style={textareaStyle}
          />
        </div>
        <button type="submit" style={buttonStyle}>
          Submit
        </button>
      </form>
    </div>
  );
};

const containerStyle: React.CSSProperties = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  height: "100vh",
  backgroundColor: "#f4f4f4",
  maxHeight: "90vh",
  overflowY: "auto",
};

const formStyle: React.CSSProperties = {
  display: "flex",
  flexDirection: "column",
  width: "300px",
  padding: "20px",
  backgroundColor: "#fff",
  borderRadius: "10px",
  boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
};

const titleStyle: React.CSSProperties = {
  marginBottom: "20px",
};

const errorStyle: React.CSSProperties = {
  color: "red",
  marginBottom: "10px",
};

const successStyle: React.CSSProperties = {
  color: "green",
  marginBottom: "10px",
};

const inputGroupStyle: React.CSSProperties = {
  marginBottom: "5px",
  textAlign: "left",
};

const labelStyle: React.CSSProperties = {
  marginBottom: "5px",
  display: "block",
  fontWeight: "bold",
};

const inputStyle: React.CSSProperties = {
  width: "calc(100% - 10px)",
  padding: "8px",
  border: "1px solid #ccc",
  borderRadius: "5px",
};

const textareaStyle: React.CSSProperties = {
  width: "calc(100% - 10px)",
  padding: "8px",
  border: "1px solid #ccc",
  borderRadius: "5px",
  resize: "none",
  height: "80px",
};

const buttonStyle: React.CSSProperties = {
  padding: "10px",
  backgroundColor: "#007BFF",
  color: "white",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
  fontSize: "16px",
};

export default SubmitTicketPage;
