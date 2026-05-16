import { useEffect, useState } from "react";
import axios from "axios";
 import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../styles/hospitalDashboard.css";

function HospitalDashboard() {

  const user = JSON.parse(localStorage.getItem("user"));
const email = user?.email;

  const [page, setPage] = useState("home");
  const [stocks, setStocks] = useState([]);
  const [requests, setRequests] = useState([]);
  const [errors, setErrors] = useState({});
  const bloodGroups = ["A+", "B+",  "O+", "AB+"];

  const [form, setForm] = useState({
    hospitalname: "",
    bloodGroup: "",
    units: 1,
    city: "",
    email: email || "",
  });

  // ================= STOCK =================
  const fetchStocks = async () => {
    try {
      const res = await axios.get("http://localhost:8080/bloodstock/all");
      setStocks(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  // ================= HISTORY =================
  const fetchHistory = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8080/admin/blood-requests?email=${email}`
      );
      setRequests(res.data);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchStocks();
    fetchHistory();
  }, []);
  const validate = () => {
  let tempErrors = {};

  if (!form.hospitalname.trim()) {
    tempErrors.hospitalname = "Hospital name is required";
  }

  if (!form.bloodGroup) {
    tempErrors.bloodGroup = "Please select blood group";
  }

  if (!form.units || form.units <= 0) {
    tempErrors.units = "Units must be greater than 0";
  }

  if (!form.city.trim()) {
    tempErrors.city = "City is required";
  }

  setErrors(tempErrors);

  return Object.keys(tempErrors).length === 0;
};

  // ================= REQUEST =================
 const handleSubmit = async (e) => {
  e.preventDefault();

  if (!validate()) return;

  

  try {
    const res = await axios.post(
      "http://localhost:8080/blood-request/create",
      form
    );

    toast.success(res.data.message);

    setForm({
      hospitalname: "",
      bloodGroup: "",
      units: 1,
      city: "",
      email: email,
    });

    setErrors({});
    fetchHistory();

  } catch (err) {
    toast.error("Request failed");
  }
};

  // ================= LOGOUT =================
  const logout = () => {
    localStorage.clear();
    window.location.href = "/login";
  };

  return (
    <div className="hospital-container">

      {/* ================= NAVBAR ================= */}
      <div className="hospital-navbar">
        <h2>🏥 Hospital Panel</h2>

        <div className="nav-links">
          {/* <button onClick={() => setPage("home")}>Home</button> */}
          <button onClick={() => setPage("stock")}>Stock</button>
          <button onClick={() => setPage("request")}>Request Blood</button>
          <button onClick={() => setPage("history")}>History</button>
          <button onClick={logout} className="logout">Logout</button>
        </div>
      </div>

      {/* ================= HOME ================= */}
      {/* {page === "home" && (
        <div className="card">
          <h2>Welcome Hospital 🏥</h2>
          <p>Manage blood requests and view available stock.</p>
        </div>
      )} */}

      {/* ================= STOCK ================= */}
      {page === "stock" && (
        <div className="card">
          <h3>Available Blood Stock</h3>

          <table>
            <thead>
              <tr>
                <th>Blood Group</th>
                <th>Units</th>
              </tr>
            </thead>

            <tbody>
              {stocks.map((s, i) => (
                <tr key={i}>
                  <td>{s.bloodGroup}</td>
                  <td>{s.units}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* ================= REQUEST ================= */}
      {page === "request" && (
        <div className="card">
          <h3>Request Blood</h3>

          <form onSubmit={handleSubmit}>

            <input
              placeholder="Hospital Name"
              value={form.hospitalname}
              onChange={(e) =>
                setForm({ ...form, hospitalname: e.target.value })
              }
            />

           <select
  value={form.bloodGroup}
  onChange={(e) =>
    setForm({ ...form, bloodGroup: e.target.value })
  }
>
  <option value="">Select Blood Group</option>

  {bloodGroups.map((bg, index) => (
    <option key={index} value={bg}>
      {bg}
    </option>
  ))}
</select>

{/* 🔴 Validation Error */}
{errors.bloodGroup && (
  <p style={{ color: "red", fontSize: "12px" }}>
    {errors.bloodGroup}
  </p>
)}

            <input
              type="number"
              placeholder="Units"
              value={form.units}
              onChange={(e) =>
                setForm({ ...form, units: e.target.value })
              }
            />

            <input
              placeholder="City"
              value={form.city}
              onChange={(e) =>
                setForm({ ...form, city: e.target.value })
              }
            />

            <input value={form.email} disabled />

            <button className="btn" type="submit">
              Submit Request
            </button>

          </form>
        </div>
      )}

      {/* ================= HISTORY ================= */}
      {page === "history" && (
        <div className="card">
          <h3>Request History</h3>

          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Hospital</th>
                <th>Blood Group</th>
                <th>Units</th>
                <th>City</th>
                 <th>Status</th>
                 {/*
                <th>Center</th>
                <th>Address</th>
                <th>Contact</th> */}
              </tr>
            </thead>

            <tbody>
              {requests.length === 0 ? (
                <tr>
                  <td colSpan="9">No Requests Found</td>
                </tr>
              ) : (
                requests.map((r, i) => (
                  <tr key={i}>
                    <td>{r.id}</td>
                    <td>{r.hospitalname}</td>
                    <td>{r.bloodGroup}</td>
                    <td>{r.units}</td>
                    <td>{r.city}</td>

                    <td>
  <span
    className={`status ${
      r.status === "APPROVED"
        ? "approved"
        : r.status === "REJECTED"
        ? "rejected"
        : "pending"
    }`}
  >
    {r.status}
  </span>
</td>

                    {/* <td>{r.centerName || "-"}</td>
                    <td>{r.address || "-"}</td>
                    <td>{r.contact || "-"}</td> */}
                  </tr>
                ))
              )}
            </tbody>

          </table>
        </div>
      )}

    </div>
  );
}

export default HospitalDashboard;