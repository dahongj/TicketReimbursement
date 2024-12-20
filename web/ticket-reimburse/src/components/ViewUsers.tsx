import React, { useState, useEffect } from "react";
import managerService from "../services/managerService";

interface User {
  accountId: number;
  username: string;
  role: string;
}

const ViewUsers: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [error, setError] = useState<string>("");
  const [editedRoles, setEditedRoles] = useState<Record<number, string>>({});

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await managerService.getAllUsers();
        setUsers(data);
      } catch (err) {
        setError("Failed to fetch users.");
      }
    };

    fetchUsers();
  }, []);

  const handleRoleSelect = (userId: number, newRole: string) => {
    setEditedRoles((prevEditedRoles) => ({
      ...prevEditedRoles,
      [userId]: newRole,
    }));
  };

  const commitRoleChange = async (user: User) => {
    const newRole = editedRoles[user.accountId];
    if (!newRole) return;

    try {
      const updatedUser = { ...user, role: newRole };
      await managerService.updateUserRole(updatedUser);

      setUsers((prevUsers) =>
        prevUsers.map((u) =>
          u.accountId === user.accountId ? { ...u, role: newRole } : u
        )
      );

      setEditedRoles((prevEditedRoles) => {
        const updatedRoles = { ...prevEditedRoles };
        delete updatedRoles[user.accountId];
        return updatedRoles;
      });

      setError("");
    } catch (err) {
      setError(`Failed to update role for user ${user.accountId}.`);
    }
  };

  return (
    <div style={containerStyle}>
      <div style={tableContainerStyle}>
        <table style={tableStyle}>
          <thead style={tableHeaderStyle}>
            <tr>
              <th style={headerCellStyle}>Username</th>
              <th style={headerCellStyle}>Role</th>
              <th style={headerCellStyle}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.accountId}>
                <td style={cellStyle}>{user.username}</td>
                <td style={cellStyle}>
                  <select
                    value={editedRoles[user.accountId] || user.role || ""}
                    onChange={(e) =>
                      handleRoleSelect(user.accountId, e.target.value)
                    }
                    style={dropdownStyle}
                  >
                    <option value="Employee">Employee</option>
                    <option value="Manager">Manager</option>
                  </select>
                </td>
                <td style={cellStyle}>
                  <button
                    onClick={() => commitRoleChange(user)}
                    disabled={!editedRoles[user.accountId]}
                  >
                    Commit Change
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

const containerStyle: React.CSSProperties = {
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  minHeight: "100vh",
  backgroundColor: "#f8f9fa",
};

const tableContainerStyle: React.CSSProperties = {
  width: "80%",
  backgroundColor: "#fff",
  borderRadius: "8px",
  boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
  padding: "20px",
  overflow: "hidden",
};

const tableStyle: React.CSSProperties = {
  borderCollapse: "collapse",
  width: "100%",
  textAlign: "left",
};

const tableHeaderStyle: React.CSSProperties = {
  backgroundColor: "#f4f4f4",
};

const headerCellStyle: React.CSSProperties = {
  padding: "12px",
  border: "1px solid #ddd",
  fontWeight: "bold",
};

const cellStyle: React.CSSProperties = {
  padding: "12px",
  border: "1px solid #ddd",
};

const dropdownStyle: React.CSSProperties = {
  padding: "5px",
  borderRadius: "4px",
  border: "1px solid #ccc",
};

const commitButtonStyle: React.CSSProperties = {
  padding: "8px 12px",
  border: "none",
  borderRadius: "4px",
  backgroundColor: "#007bff",
  color: "#fff",
  cursor: "pointer",
  fontSize: "14px",
};

export default ViewUsers;
