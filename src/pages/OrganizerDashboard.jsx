import { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../styles/organizer.css";

function OrganizerDashboard() {
  const [activeTab, setActiveTab] = useState("camps");
  const [camps, setCamps] = useState([]);
  const [donations, setDonations] = useState([]);

  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user"));
  const email = user?.email;

  /* ================= FETCH CAMPS ================= */
 const fetchCamps = async () => {
  try {
    const res = await axios.get(
      `http://localhost:8080/admin/organizer/camps?email=${email}`
    );
    setCamps(res.data);
  } catch (error) {
    console.log(error);
    toast.error("Failed to fetch camps");
  }
};

useEffect(() => {
  fetchCamps();
}, []);

  /* ================= FETCH DONATIONS ================= */
 const fetchPendingDonations = async () => {
  try {
    const res = await axios.get(
      `http://localhost:8080/donation/status`
    );

    setDonations(res.data);
  } catch (error) {
    console.log(error);
    toast.error("Failed to fetch pending donations");
  }
};

  useEffect(() => {
    fetchCamps();
    fetchPendingDonations();
  }, []);

  /* ================= VERIFY ================= */
  const verifyDonation = async (id) => {
    try {
      await axios.put(
        `http://localhost:8080/admin/donations/${id}/verify?email=${email}`
      );
      toast.success("Donation Verified ✅");
      fetchDonations();
    } catch (err) {
      toast.error("Verification failed");
    }
  };

  /* ================= REJECT ================= */
  const rejectDonation = async (id) => {
    try {
      await axios.put(
        `http://localhost:8080/admin/donations/${id}/reject?email=${email}`
      );
      toast.success("Donation Rejected ❌");
      fetchDonations();
    } catch (err) {
      toast.error("Reject failed");
    }
  };

  /* ================= LOGOUT ================= */
  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div>
      {/* 🔥 NAVBAR */}
      <div className="organizer-navbar">
        <div className="organizer-logo">🏢 Organizer Panel</div>

        <div className="organizer-nav-links">
          <button
            className={`organizer-btn ${activeTab === "camps" ? "active" : ""}`}
            onClick={() => setActiveTab("camps")}
          >
            My Camps
          </button>

          <button
            className={`organizer-btn ${activeTab === "donations" ? "active" : ""}`}
            onClick={() => setActiveTab("donations")}
          >
            Donations
          </button>

          <button className="organizer-btn logout-btn" onClick={logout}>
            Logout
          </button>
        </div>
      </div>

      {/* ================= CAMPS ================= */}
      {activeTab === "camps" && (
  <div className="dashboard-content">
    <h2>🏕 My Assigned Camps</h2>

    <div className="camp-grid">
      {camps.length > 0 ? (
        camps.map((c) => (
          <div className="camp-card" key={c.id}>
            
            <h3>🏥 {c.campName}</h3>

            <span className={`status-badge ${c.status}`}>
              {c.status}
            </span>

            <p><b>📍 Location:</b> {c.location}</p>
            <p><b>📅 Date:</b> {c.campDate}</p>
            <p><b>📝 Description:</b> {c.description}</p>
            <p><b>🏠 Address:</b> {c.address}</p>

          </div>
        ))
      ) : (
        <p style={{ color: "gray" }}>No camps assigned</p>
      )}
    </div>
  </div>
)}

      {/* ================= DONATIONS ================= */}
     {activeTab === "donations" && (
  <div className="dashboard-content">
    <h2>🩸 Pending Donations</h2>

    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Blood</th>
          <th>Units</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>
        {donations.length > 0 ? (
          donations.map((d) => (
            <tr key={d.id}>
              <td>{d.name}</td>
              <td>{d.bloodGroup}</td>
              <td>{d.units}</td>
              

              <td>
                <span className={`badge ${d.status}`}>
                  {d.status}
                </span>
              </td>

              <td>
                <button
                  className="verify-btn"
                  disabled={d.status !== "PENDING"}
                  onClick={() => verifyDonation(d.id)}
                >
                  Accept
                </button>

                <button
                  className="reject-btn"
                  disabled={d.status !== "PENDING"}
                  onClick={() => rejectDonation(d.id)}
                >
                  Reject
                </button>
              </td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="6" style={{ textAlign: "center", color: "gray" }}>
              No pending donations
            </td>
          </tr>
        )}
      </tbody>
    </table>
  </div>
)}
    </div>
  );
}

export default OrganizerDashboard;