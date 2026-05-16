import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "../styles/admin.css";

function DonorDashboard() {

  const [activeTab, setActiveTab] = useState("home"); // ✅ SINGLE STATE
  const [camps, setCamps] = useState([]);
  const [history, setHistory] = useState([]);
  const [search, setSearch] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [selectedCamp, setSelectedCamp] = useState(null);
  const [searchInput, setSearchInput] = useState("");


  const [form, setForm] = useState({
    units: "",
    location: ""
  });

  const handleSearch = async () => {
  if (!search.trim()) {
    fetchCamps(); // load default (user city)
    return;
  }

  try {
    const res = await axios.get(
      `http://localhost:8080/admin/camps?location=${search}`
    );

    setCamps(res.data); // 🔥 replace camps

  } catch (err) {
    toast.error("No camps found");
    setCamps([]);
  }
};
  const navigate = useNavigate();

 const user = JSON.parse(localStorage.getItem("user"));

const userCity = user?.city;
const userEmail = user?.email;

  const showToast = (msg, type = "success") => {
    if (type === "error") toast.error(msg);
    else toast.success(msg);
  };

  /* 🔥 FETCH CAMPS */
  const fetchCamps = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8080/admin/camps?location=${userCity}`
      );

      const filtered = res.data.filter(
        (c) => c.status === "ONGOING" || c.status === "UPCOMING"
      );

      setCamps(filtered);

    } catch (err) {
      showToast("Failed to load camps", "error");
    }
  };

  /* 🔥 FETCH HISTORY */
  const fetchHistory = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8080/donation/history?email=${userEmail}`
      );
      setHistory(res.data);
    } catch (err) {
      showToast("Failed to load history", "error");
    }
  };

  useEffect(() => {
    fetchCamps();
    fetchHistory();
  }, []);

  console.log("history",history);

  /* 🔥 LOGOUT */
  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  /* 🔥 DONATE */
 const submitDonation = async () => {

  // 🔥 FRONTEND VALIDATION
  if (!form.units || form.units <= 0) {
    toast.error("⚠️ Enter valid blood units");
    return;
  }

  if (!form.location || form.location.trim() === "") {
    toast.error("📍 Enter location");
    return;
  }

  try {
    const user = JSON.parse(localStorage.getItem("user"));
    const email = user?.email;

    await axios.post(
      `http://localhost:8080/donation/donate?email=${email}`,
      {
        campId: selectedCamp.id,
        units: Number(form.units),
        location: form.location.trim()
      }
    );

    // ✅ SUCCESS
    toast.success("Donation Request Sent ✅");

    setShowModal(false);
    setForm({ units: "", location: "" });

    fetchHistory();

  } catch (err) {

    let message = "Something went wrong ❌";

    if (err.response) {
      if (typeof err.response.data === "string") {
        message = err.response.data;
      } else if (err.response.data?.message) {
        message = err.response.data.message;
      }
    }

    // 🔥 CUSTOM ERROR HANDLING
    if (message.includes("Units")) {
      message = "⚠️ Please enter valid blood units";
    }
    else if (message.includes("Location")) {
      message = "📍 Please enter your location";
    }
    else if (message.includes("Already donated")) {
      message = "🛑 You already donated in this camp";
    }
    else if (message.includes("donate after")) {
      message = message; // keep backend message with date
    }

    toast.error(message);
  }
};

  console.log("camps:",camps);

  /* 🔍 SEARCH */
  const filteredCamps = camps.filter((c) =>
  c.campName.toLowerCase().includes(search.toLowerCase())
);

  return (
    <div>

      {/* 🔥 NAVBAR */}
      <div className="donor-navbar">

        <div className="donor-logo">🩸 Donor Panel</div>

        <div className="donor-nav-links">

          <button
            className={`donor-btn ${activeTab === "home" ? "active" : ""}`}
            onClick={() => setActiveTab("home")}
          >
            Home
          </button>

          <button
            className={`donor-btn ${activeTab === "history" ? "active" : ""}`}
            onClick={() => setActiveTab("history")}
          >
            My Donations
          </button>

          <button className="donor-btn logout-btn" onClick={logout}>
            Logout
          </button>

        </div>
      </div>

      {/* 🔥 HOME */}
      {activeTab === "home" && (
        <div className="dashboard-content">

          {/* 🔍 SEARCH */}
          <div className="search-box">

  <input
  placeholder="Search by Location..."
  value={search}
  onChange={(e) => setSearch(e.target.value)}
/>

<button onClick={handleSearch}>
  🔍 Search
</button>

</div>

          {/* 🏕 CAMPS */}
          <div className="premium-grid">
  {camps.length > 0 ? (
    camps.map((c) => (
      <div className="glass-card" key={c.id}>

        <h3>🏕 {c.campName}</h3>

        <span className={`badge ${c.status}`}>
          {c.status}
        </span>

        <p><b>📍 Location:</b> {c.location}</p>
        <p><b>📅 Date:</b> {c.campDate}</p>
        <p><b>🏢 Address:</b> {c.address}</p>
        <p><b>👤 Organizer:</b> {c.organizerName}</p>

       <button
  className="donate-btn"
  disabled={c.status === "COMPLETED" || c.status === "UPCOMING"}
  onClick={() => {
    setSelectedCamp(c);
    setShowModal(true);
  }}
>
  {c.status === "UPCOMING"
    ? "Coming Soon"
    : c.status === "COMPLETED"
    ? "Closed"
    : "Donate"}
</button>

      </div>
    ))
  ) : (
    <p style={{ textAlign: "center", width: "100%" }}>
      ❌ No camps found
    </p>
  )}
</div>

        </div>
      )}

      {/* 🔥 HISTORY */}
      {activeTab === "history" && (
       <div className="dashboard-content">
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Blood Group</th>
        <th>Date</th>
        <th>Status</th>
      </tr>
    </thead>

    <tbody>
      {history.length > 0 ? (
        history.map((d) => (
          <tr key={d.id}>
            <td>{d.id}</td>
            <td>{d.bloodGroup}</td>
            
  
  <td>
  {d.date
    ? typeof d.date === "string"
      ? new Date(d.date).toLocaleDateString("en-IN")
      : new Date(d.date.year, d.date.month - 1, d.date.day)
          .toLocaleDateString("en-IN")
    : "N/A"}
</td>

            <td>
              <span className={`badge ${d.status}`}>
                {d.status === "PENDING" && "🟡 Pending"}
                {d.status === "APPROVED" && "🟢 Approved"}
                {d.status === "REJECTED" && "🔴 Rejected"}
              </span>
            </td>
          </tr>
        ))
      ) : (
        <tr>
          <td colSpan="4" style={{ textAlign: "center" }}>
            ❌ No donation history
          </td>
        </tr>
      )}
    </tbody>
  </table>
</div>
      )}

      {/* 🔥 MODAL */}
     {showModal && (
  <div className="modal-overlay">
    <div className="modal-box">

      <h2>Donate at {selectedCamp.campName}</h2>

      {/* 📍 LOCATION */}
      <input
        placeholder="Enter Location"
        value={form.location}
        onChange={(e) =>
          setForm({ ...form, location: e.target.value })
        }
      />

      {/* 🩸 UNITS */}
      <input
        type="number"
        placeholder="Enter Units"
        value={form.units}
        onChange={(e) =>
          setForm({ ...form, units: e.target.value })
        }
      />

      <div className="modal-actions">
        <button onClick={() => setShowModal(false)}>
          Cancel
        </button>

       <button
  onClick={submitDonation}
  disabled={!form.units || form.units <= 0 || !form.location}
>
  Submit
</button>
      </div>

    </div>
  </div>
)}

    </div>
  );
}

export default DonorDashboard;