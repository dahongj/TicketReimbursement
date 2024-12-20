import React from "react";
import { Link } from "react-router-dom";

interface NavbarProps {
  isLoggedIn: boolean;
  role: string;
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ isLoggedIn, role, onLogout }) => {
  return (
      <nav style={navbarStyle}>
          <div style={containerStyle}>
              {isLoggedIn ? (
                  <>
                      {role === 'Manager' && (
                          <>
                              <Link to="/manager-dashboard" style={boxStyle}>
                                  Dashboard
                              </Link>
                              <Link to="/view-users" style={boxStyle}>
                                  View Users
                              </Link>
                          </>
                      )}
                      {role === 'Employee' && (
                          <>
                              <Link to="/view-tickets" style={boxStyle}>
                                  View Tickets
                              </Link>
                              <Link to="/submit-ticket" style={boxStyle}>
                                  Submit Ticket
                              </Link>
                          </>
                      )}
                      <button onClick={onLogout} style={boxStyle}>
                          Logout
                      </button>
                  </>
              ) : (
                  <>
                      <Link to="/login" style={boxStyle}>
                          Login
                      </Link>
                      <Link to="/register" style={boxStyle}>
                          Register
                      </Link>
                  </>
              )}
          </div>
      </nav>
  );
};

const navbarStyle: React.CSSProperties = {
  backgroundColor: '#333',
  padding: '10px 20px',
};

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'row',
  gap: '10px',
  alignItems: 'center',
};

const boxStyle: React.CSSProperties = {
  padding: '10px 15px',
  backgroundColor: '#555',
  color: 'white',
  textDecoration: 'none',
  borderRadius: '5px',
  textAlign: 'center',
  cursor: 'pointer',
};

export default Navbar;
