import React, { useState } from "react";
import { register } from "../services/authService";
import { useNavigate } from "react-router-dom";

const Register: React.FC = () => {
  const [formData, setFormData] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    setError('');
    e.preventDefault();
    try {
      await register(formData);
      setSuccess("Registration successful! Redirecting to login...");
      setTimeout(() => navigate("/login"), 3000);
    } catch (error) {
      setError("Username already exists.");
    }
  };

  return (
    <div style={containerStyle}>
      <div style={formStyle}>
        <h2 style={titleStyle}>Register</h2>
        {error && <p style={errorStyle}>{error}</p>}
        {success && <p style={successStyle}>{success}</p>}
        <form onSubmit={handleSubmit}>
          <div style={inputGroupStyle}>
            <label htmlFor="username" style={labelStyle}>
              Username
            </label>
            <input
              type="text"
              name="username"
              placeholder="Choose a username"
              onChange={handleChange}
              required
            />
          </div>
          <div style={inputGroupStyle}>
            <label htmlFor="password" style={labelStyle}>
              Password
            </label>
            <input
              type="text"
              name="password"
              placeholder="Choose a password"
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" style={buttonStyle}>
            Register
          </button>
        </form>
        <p style={switchPageText}>
          Already have an account?{" "}
          <a href="/login" style={linkStyle}>
            Login
          </a>
        </p>
      </div>
    </div>
  );
};

const containerStyle: React.CSSProperties = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  height: "100vh",
  backgroundColor: "#f4f7fc",
  maxHeight: "90vh",
  overflowY: "auto",
};

const formStyle: React.CSSProperties = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  padding: "30px 40px",
  backgroundColor: "#fff",
  borderRadius: "8px",
  boxShadow: "0 8px 16px rgba(0, 0, 0, 0.1)",
  width: "300px",
  maxHeight: "90vh",
  overflowY: "auto",
};

const titleStyle: React.CSSProperties = {
  fontSize: "24px",
  marginBottom: "20px",
  fontWeight: "bold",
  color: "#333",
};

const errorStyle: React.CSSProperties = {
  color: "red",
  marginBottom: "15px",
  textAlign: "center",
};

const inputGroupStyle: React.CSSProperties = {
  display: "flex",
  flexDirection: "column",
  marginBottom: "20px",
  width: "100%",
};

const labelStyle: React.CSSProperties = {
  marginBottom: "8px",
  fontSize: "14px",
  color: "#555",
};

const inputStyle: React.CSSProperties = {
  width: "100%",
  padding: "12px",
  border: "1px solid #ccc",
  borderRadius: "5px",
  fontSize: "14px",
  marginBottom: "10px",
  backgroundColor: "#f7f7f7",
  boxSizing: "border-box",
};

const buttonContainerStyle: React.CSSProperties = {
  display: "flex",
  justifyContent: "center",
  width: "100%",
};

const buttonStyle: React.CSSProperties = {
  padding: "10px",
  backgroundColor: "#4CAF50",
  color: "white",
  border: "none",
  borderRadius: "5px",
  cursor: "pointer",
  fontSize: "16px",
  width: "100%",
};

const switchPageText: React.CSSProperties = {
  marginTop: "10px",
  fontSize: "14px",
  textAlign: "center",
  color: "#555",
};

const linkStyle: React.CSSProperties = {
  color: "#4CAF50",
  textDecoration: "none",
  fontWeight: "bold",
};

const successStyle: React.CSSProperties = {
    color: 'green',
    marginBottom: '15px',
    textAlign: 'center',
    fontWeight: 'bold',
};

export default Register;
