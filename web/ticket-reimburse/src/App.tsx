import React, { useState } from "react";
import {
  BrowserRouter as Router,
  useNavigate,
  Route,
  Routes,
} from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./components/Login";
import Register from "./components/Register";
import ManagerDashboard from "./components/ManagerDashboard";
import SubmitTicket from "./components/SubmitTicket";
import ViewTickets from "./components/ViewTicket";
import ViewUsers from "./components/ViewUsers";

const App: React.FC = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState("");
  const [accountId, setAccountId] = useState(0);
  const navigate = useNavigate();

  const handleLogin = (id: number, userRole: string) => {
    setIsLoggedIn(true);
    setRole(userRole);
    setAccountId(id);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setRole("");
    setAccountId(0);
    navigate("/login");
  };

  return (
    <div className="App">
      <Navbar isLoggedIn={isLoggedIn} role={role} onLogout={handleLogout} />
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/register" element={<Register />} />
        {isLoggedIn && role === "Employee" && (
          <>
            <Route
              path="/submit-ticket"
              element={<SubmitTicket accountId={accountId} />}
            />
            <Route
              path="/view-tickets"
              element={<ViewTickets accountId={accountId} />}
            />
          </>
        )}
        {isLoggedIn && role === "Manager" && (
          <>
            <Route path="/manager-dashboard" element={<ManagerDashboard />} />
            <Route path="/view-users" element={<ViewUsers />} />
          </>
        )}
      </Routes>
    </div>
  );
};

export default App;
