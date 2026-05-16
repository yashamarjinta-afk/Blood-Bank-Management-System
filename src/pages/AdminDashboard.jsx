import { useState, useEffect } from "react";
import axios from "axios";
import { toast} from "react-toastify";
import "../styles/admin.css";
import { useNavigate } from "react-router-dom";


export default function AdminDashboard() {
  const [active, setActive] = useState("dashboard");
   const navigate = useNavigate();   // ✅ add this
   const [requests, setRequests] = useState([]);

   const [centers, setCenters] = useState([]);
const [selectedCenters, setSelectedCenters] = useState({});

const fetchCenters = async () => {
  const res = await axios.get("http://localhost:8080/admin/storage-centers");
  setCenters(res.data);
};

const handleCenterChange = (requestId, centerId) => {
  setSelectedCenters({
    ...selectedCenters,
    [requestId]: centerId
  });
};

const fetchPending = async () => {
  try {
    const res = await axios.get("http://localhost:8080/blood-request/pending");
    setRequests(res.data);
  } catch (err) {
    console.log(err);
  }
};

useEffect(() => {
  fetchCenters();
  fetchPending();
}, []);

  const showToast = (msg) => {
    toast.success(msg);
  };

   // ✅ LOGOUT FUNCTION
  const handleLogout = () => {
    localStorage.clear();
    toast.success("Logged out successfully");
    navigate("/login");
  };

  return (
    <div className="admin-container">
      

      {/* HEADER */}
     <header className="admin-header">
  <h2>🩸 Admin Panel</h2>

  <div className="nav-buttons">
    {["dashboard","organizer","camp","storage","donations","requests","stock","users"].map(item => (
      <button
        key={item}
        className={active === item ? "active" : ""}
        onClick={() => setActive(item)}
      >
        {item}
      </button>
    ))}

    {/* ✅ LOGOUT BUTTON */}
    <button className="logout-btn" onClick={handleLogout}>
      Logout
    </button>
  </div>
</header>

      {/* MAIN */}
      <div className="admin-main">
        {active === "dashboard" && <DashboardStats />}
        {active === "organizer" && <CreateOrganizer showToast={showToast} />}
        {active === "camp" && <CreateCamp showToast={showToast} />}
        {active === "storage" && <CreateStorage showToast={showToast} />}
        {active === "donations" && <DonationTable showToast={showToast} />}
        {active === "requests" && (
  <RequestTable
    showToast={showToast}
    centers={centers}
    selectedCenters={selectedCenters}
    handleCenterChange={handleCenterChange}
    fetchPending={fetchPending}
  />
)}
        {active === "stock" && <StockTable showToast={showToast} />}
        {active === "users" && <UsersTable />}
      </div>
    </div>
  );
}

/* ================= DASHBOARD ================= */
function DashboardStats() {
  const [stats, setStats] = useState({});

  useEffect(() => {
    Promise.all([
      axios.get("http://localhost:8080/admin/users"),
      axios.get("http://localhost:8080/donation/pending"),
      axios.get("http://localhost:8080/admin/blood-requests?email=test@gmail.com"),
      axios.get("http://localhost:8080/bloodstock/by-blood-group?bloodGroup=A+")
    ]).then(([u, d, r, s]) => {
      setStats({
        users: u.data.length,
        donations: d.data.length,
        requests: r.data.length,
        stock: s.data.reduce((a,b)=>a+b.units,0)
      });
    });
  }, []);

  return (
    <div className="grid">
      <Card title="Users" value={stats.users} />
      <Card title="Pending Donations" value={stats.donations} />
      <Card title="Requests" value={stats.requests} />
      
    </div>
  );
}

/* ================= ORGANIZER ================= */
function CreateOrganizer({ showToast }) {
  const [organizers, setOrganizers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
const [selectedId, setSelectedId] = useState(null);
const [errors, setErrors] = useState({});
const validate = () => {
  let err = {};

  if (!form.name) err.name = "Name is required";

  if (!form.email) err.email = "Email is required";
  else if (!/\S+@\S+\.\S+/.test(form.email))
    err.email = "Invalid email";

  if (!form.password) err.password = "Password is required";
  else if (form.password.length < 6)
    err.password = "Minimum 6 characters";

  if (!form.city) err.city = "City is required";

  if (!form.bloodGroup) err.bloodGroup = "Select blood group";

  setErrors(err);

  return Object.keys(err).length === 0;
};
const openEdit = (o) => {
  setForm(o);           // prefill data
  setSelectedId(o.id);
  setEditMode(true);
  setShowModal(true);
};

const deleteOrganizer = async (id) => {
  if (!window.confirm("Delete this organizer?")) return;

  try {
    const res = await axios.delete(
      `http://localhost:8080/admin/deleteorganizers/${id}`
    );

    showToast(res.data?.message || "Deleted successfully");
    fetchOrganizers();

  } catch (err) {
    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "Delete failed";

    showToast(msg, "error");
  }
};

const closeModal = () => {
  setShowModal(false);
  setEditMode(false);
};

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    city: "",
    bloodGroup: ""
  });

  const fetchOrganizers = async () => {
  try {
    const res = await axios.get(
      "http://localhost:8080/admin/getallorganizers"
    );
    setOrganizers(res.data);
  } catch (err) {
    showToast("Failed to load organizers", "error");
  }
};

  useEffect(() => {
    fetchOrganizers();
  }, []);

  const submit = async () => {
  if (!validate()) return;

  try {
    if (editMode) {
      const res = await axios.put(
        `http://localhost:8080/admin/updateorganizers/${selectedId}`,
        form
      );

      showToast(res.data?.message || "Updated successfully");
    } else {
      const res = await axios.post(
        "http://localhost:8080/admin/organizers",
        form
      );

      showToast(res.data?.message || "Created successfully");
    }

    setShowModal(false);
    setErrors({});
    fetchOrganizers();

  } catch (err) {
    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "Operation failed";

    showToast(msg, "error");
  }
};
  return (
    <div className="section-card">

      {/* 🔥 HEADER */}
     <div className="section-header">
  <div className="section-title">
    <h2>Organizers</h2>
    <p>Manage and create organizers</p>
  </div>

  <button
  className="add-organizer-btn"
  onClick={() => {
    setEditMode(false);   // ✅ important
    setForm({             // ✅ reset form
      name: "",
      email: "",
      password: "",
      city: "",
      bloodGroup: ""
    });
    setShowModal(true);
  }}
>
  + Add Organizer
</button>
</div>

      {/* 🔥 CARDS */}
      <div className="premium-grid">
  {organizers.map(o => (
    <div className="glass-card" key={o.id}>

      {/* TOP STRIP */}
      <div className="card-accent"></div>

      <div className="card-content">

        <div className="card-row">
          <div className="avatar-lg">{o.name.charAt(0)}</div>
          <div>
            <h3>{o.name}</h3>
            <p className="muted">{o.email}</p>
          </div>
        </div>

        <div className="divider"></div>

        <div className="card-details">
          <div>
            <span className="label">City</span>
            <p>{o.city}</p>
          </div>

          <div>
            <span className="label">Blood</span>
            <p className="blood">{o.bloodGroup}</p>
          </div>
        </div>

      </div>

<div className="card-actions">
  <button className="edit-btn" onClick={() => openEdit(o)}>Edit</button>
  <button className="delete-btn" onClick={() => deleteOrganizer(o.id)}>Delete</button>
</div>
    </div>
  ))}
</div>

      {/* 🔥 POPUP MODAL */}
      {showModal && (
  <div className="modal-overlay">
    <div className="modal-box">

      <div className="modal-header">
        <h2>{editMode ? "Update Organizer" : "Add Organizer"}</h2>
        <span onClick={closeModal}>✖</span>
      </div>

      {/* ✅ INPUTS HERE */}
      <div className="form-group">
  <label>Name</label>
  <input
    value={form.name}
    placeholder="Enter name"
    onChange={e => setForm({ ...form, name: e.target.value })}
  />
  {errors.name && <span className="error">{errors.name}</span>}
</div>

<div className="form-group">
  <label>Email</label>
  <input
    value={form.email}
    placeholder="Enter email"
    onChange={e => setForm({ ...form, email: e.target.value })}
    
  />
   {errors.email && <span className="error">{errors.email}</span>}
</div>

<div className="form-group">
  <label>Password</label>
  <input
    type="password"
    value={form.password}
    placeholder="Enter password"
    onChange={e => setForm({ ...form, password: e.target.value })}
  />
  {errors.password && <span className="error">{errors.password}</span>}
</div>

<div className="form-group">
  <label>City</label>
  <input
    value={form.city}
    placeholder="Enter city"
    onChange={e => setForm({ ...form, city: e.target.value })}
  />
  {errors.city && <span className="error">{errors.city}</span>}
</div>

<div className="form-group">
  <label>Blood Group</label>
  <select
    value={form.bloodGroup}
    onChange={e => setForm({ ...form, bloodGroup: e.target.value })}
  >
    <option value="">Select Blood Group</option>
    <option value="A+">A+</option>
    <option value="O+">O+</option>
    <option value="B+">B+</option>
    <option value="AB+">AB+</option>
  </select>
  {errors.bloodGroup && <span className="error">{errors.bloodGroup}</span>}
</div>

      <div className="modal-actions">
        <button onClick={() => setShowModal(false)}>Cancel</button>
        <button onClick={submit}>
          {editMode ? "Update" : "Create"}
        </button>
      </div>

    </div>
  </div>
)}
    </div>
  );
}

/* ================= CAMP ================= */
function CreateCamp({ showToast }) {
  const [list, setList] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [organizers, setOrganizers] = useState([]);
const fetchOrganizers = async () => {
  try {
    const res = await axios.get("http://localhost:8080/admin/getallorganizers");
    setOrganizers(res.data);
  } catch (err) {
    showToast("Failed to load organizers", "error");
  }
};

useEffect(() => {
  fetchData();
  fetchOrganizers(); // ✅ ADD THIS
}, []);

  const [form, setForm] = useState({
  campName: "",
  location: "",
  campDate: "",
  description: "",
  contactNumber: "",
  organizerId: "",
  address: "",
  status: "" // optional
});

  const [errors, setErrors] = useState({});

  const fetchData = async () => {
  try {
    const res = await axios.get("http://localhost:8080/admin/getallcamps");
    setList(res.data);
  } catch (err) {
    showToast("Failed to load camps", "error");
  }
};

  

  /* 🔥 VALIDATION */
 const validate = () => {
  let err = {};

  if (!form.campName) err.campName = "Required";
  if (!form.location) err.location = "Required";
  if (!form.campDate) err.campDate = "Required";

  if (!form.description) err.description = "Required";
  else if (form.description.length < 10)
    err.description = "Min 10 characters";

  if (!form.contactNumber) err.contactNumber = "Required";
  else if (!/^[0-9]{10}$/.test(form.contactNumber))
    err.contactNumber = "Must be 10 digits";

  if (!form.organizerId) err.organizerId = "Required";

  setErrors(err);
  return Object.keys(err).length === 0;
};

  /* 🔥 OPEN EDIT */
 const openEdit = (c) => {
  setForm({
    campName: c.campName || "",
    location: c.location || "",
    address: c.address || "",
    organizerId: c.organizerId || "",
    campDate: c.campDate ? c.campDate.substring(0,10) : "",
    description: c.description || "",
    contactNumber: c.contactNumber || "",
    status: c.status || ""   // 🔥 IMPORTANT
  });

  setSelectedId(c.id);
  setEditMode(true);
  setShowModal(true);
};

  /* 🔥 DELETE */
  const deleteItem = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/admin/camps/${id}`);
    showToast("Deleted successfully");
    fetchData();
  } catch (err) {
    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "Delete failed";

    showToast(msg, "error");
  }
};

 const [time,settime]=useState();

 

 

  /* 🔥 SUBMIT */
  const submit = async () => {
  if (!validate()) return;

  try {
    if (editMode) {
      await axios.put(
        `http://localhost:8080/admin/updatecamps/${selectedId}`,
        form
      );
      showToast("Updated successfully");
    } else {
      await axios.post("http://localhost:8080/admin/camps", form);
      showToast("Created successfully");
    }

    setShowModal(false);
    setEditMode(false);
    fetchData();
    setErrors({});
  } catch (err) {
    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "Operation failed";

    showToast(msg, "error");
  }
};

  return (
    <div>

      {/* HEADER */}
      <div className="section-header">
        <h2>Camps</h2>
        <button className="add-organizer-btn" onClick={() => {
  setEditMode(false);
  setForm({
    campName: "",
    location: "",
    campDate: "",
    description: "",
    contactNumber: "",
    organizerId: "",
    address: "",
    status: ""
  });
  setErrors({});
  setShowModal(true);
}}>
          + Add Camp
        </button>
      </div>

      {/* CARDS */}
      <div className="premium-grid">
  {list.map(c => {
  const isLocked = c.status === "COMPLETED" || c.status === "ONGOING";

  return (
    <div className="glass-card" key={c.id}>

      {/* TOP STRIP */}
      <div className="card-accent"></div>

      <div className="card-content">

        {/* HEADER */}
        <div className="card-header">
          <h3>🏕 {c.campName}</h3>
          <span className={`status ${c.status?.toLowerCase()}`}>
            {c.status}
          </span>
        </div>

        {/* DETAILS */}
        <div className="card-info">
          <p>📍 <strong>Location:</strong> {c.location}</p>
          <p>🏠 <strong>Address:</strong> {c.address}</p>
          <p>📅 <strong>Date:</strong> {c.campDate}</p>
          
          <p>📞 <strong>Contact:</strong> {c.contactNumber}</p>
        </div>

        {/* DESCRIPTION */}
        <div className="card-desc">
          <p>{c.description}</p>
        </div>

        {/* ORGANIZER */}
        <div className="organizer-tag">
          👤 Organized by <span>{c.organizerName}</span>
        </div>

        {/* MESSAGE */}
        {c.message && (
          <div className="message-box">
            💬 {c.message}
          </div>
        )}

        {/* ACTIONS */}
        <div className="card-actions">

          {!isLocked && (
            <button className="edit-btn" onClick={() => openEdit(c)}>
              Edit
            </button>
          )}

          {isLocked && (
            <span className="disabled-text">{c.status}</span>
          )}

          <button className="delete-btn" onClick={() => deleteItem(c.id)}>
            Delete
          </button>

        </div>

      </div>
    </div>
  );
})}
</div>

      {/* MODAL */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">

            <div className="modal-header">
  <h2>{editMode ? "Update Camp" : "Add Camp"}</h2>
  <span className="close-btn" onClick={() => setShowModal(false)}>✖</span>
</div>

            <div className="form-group">
  <label>Camp Name</label>
  <input
    value={form.campName}
    onChange={e => setForm({ ...form, campName: e.target.value })}
    placeholder="Enter camp name"
  />
  {errors.campName && <span className="error">{errors.campName}</span>}
</div>

<div className="form-group">
  <label>Location</label>
  <input
    value={form.location}
    onChange={e => setForm({ ...form, location: e.target.value })}
    placeholder="Enter location"
  />
  {errors.location && <span className="error">{errors.location}</span>}
</div>

<div className="form-group">
  <label>Address (Optional)</label>
  <input
    value={form.address}
    onChange={e => setForm({ ...form, address: e.target.value })}
    placeholder="Enter full address"
  />
</div>

<div className="form-group">
  <label>Camp Date</label>
  <input
    type="date"
    value={form.campDate}
    onChange={e => setForm({ ...form, campDate: e.target.value })}
  />
  {errors.campDate && <span className="error">{errors.campDate}</span>}
</div>

<div className="form-group">
  <label>Description</label>
  <textarea
    value={form.description}
    onChange={e => setForm({ ...form, description: e.target.value })}
    placeholder="Enter description (min 10 characters)"
  />
  {errors.description && <span className="error">{errors.description}</span>}
</div>

<div className="form-group">
  <label>Contact Number</label>
  <input
    value={form.contactNumber}
    onChange={e => setForm({ ...form, contactNumber: e.target.value })}
    placeholder="Enter 10 digit number"
  />
  {errors.contactNumber && <span className="error">{errors.contactNumber}</span>}
</div>

<div className="form-group">
  <label>Organizer</label>
  <select
    value={form.organizerId || ""}
    onChange={e => setForm({ ...form, organizerId: Number( e.target.value) })}
  >
    <option value="">Select Organizer</option>
    {organizers.map(o => (
      <option key={o.id} value={o.id}>
        {o.name}
      </option>
    ))}
  </select>
  {errors.organizerId && <span className="error">{errors.organizerId}</span>}
</div>



            <div className="modal-actions">
  <button onClick={() => setShowModal(false)}>Cancel</button>
  <button className="primary-btn" onClick={submit}>
    {editMode ? "Update Camp" : "Create Camp"}
  </button>
</div>
          </div>
        </div>
      )}
    </div>
  );
}

/* ================= STORAGE ================= */
function CreateStorage({ showToast }) {
  const [list, setList] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [selectedId, setSelectedId] = useState(null);

  const [form, setForm] = useState({
    name: "",
    city: "",
    address: "",
    contact: ""
  });

  const [errors, setErrors] = useState({});

  /* 🔥 FETCH */
  const fetchData = async () => {
    const res = await axios.get("http://localhost:8080/admin/storage-centers");
    setList(res.data);
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* 🔥 VALIDATION */
  const validate = () => {
    let err = {};

    if (!form.name) err.name = "Required";
    if (!form.city) err.city = "Required";

    if (!form.address) err.address = "Required";
    else if (form.address.length < 10)
      err.address = "Min 10 characters";

    if (!form.contact) err.contact = "Required";
    else if (!/^[0-9]{10}$/.test(form.contact))
      err.contact = "Must be 10 digits";

    setErrors(err);
    return Object.keys(err).length === 0;
  };

  /* 🔥 OPEN EDIT */
  const openEdit = (s) => {
    setForm({
      name: s.name || "",
      city: s.city || "",
      address: s.address || "",
      contact: s.contact || ""
    });

    setSelectedId(s.id);
    setEditMode(true);
    setShowModal(true);
  };

  /* 🔥 DELETE */
 const deleteItem = async (id) => {
  try {
    const res = await axios.delete(`http://localhost:8080/admin/deletestoragecenter/${id}`);
    showToast(res.data || "Deleted successfully ✅");
    fetchData();
  } catch (err) {
    const msg =
      err.response?.data ||
      err.response?.data?.message ||
      err.message ||
      "Something went wrong ❌";

    showToast(msg);
  }
};

  /* 🔥 SUBMIT */
  const submit = async () => {
  if (!validate()) return;

  try {
    let res;

    if (editMode) {
      res = await axios.put(
        `http://localhost:8080/admin/updatestoragecenter/${selectedId}`,
        form
      );
      showToast(res.data || "Updated successfully ✅");
    } else {
      res = await axios.post(
        "http://localhost:8080/admin/storage-centers",
        form
      );
      showToast(res.data.message || "Created successfully ✅");
    }

    setShowModal(false);
    setEditMode(false);
    fetchData();

  } catch (err) {
    console.log(err); // debug

    let msg = "Something went wrong ❌";

    if (err.response) {
      // Backend response exists
      if (typeof err.response.data === "string") {
        msg = err.response.data;
      } else if (err.response.data.message) {
        msg = err.response.data.message;
      }
    } else if (err.message) {
      msg = err.message;
    }

    showToast(msg);
  }
};

  return (
    <div>

      {/* 🔥 HEADER */}
      <div className="section-header">
        <h2>Storage Centers</h2>

        <button
          className="add-organizer-btn"
          onClick={() => {
            setEditMode(false);
            setForm({
              name: "",
              city: "",
              address: "",
              contact: ""
            });
            setErrors({});
            setShowModal(true);
          }}
        >
          + Add Storage
        </button>
      </div>

      {/* 🔥 CARDS */}
      <div className="premium-grid">
        {list.map(s => (
          <div className="glass-card" key={s.id}>

            <div className="card-accent"></div>

            <div className="card-content">

              {/* HEADER */}
              <div className="card-header">
                <h3>🏥 {s.name}</h3>
              </div>

              {/* DETAILS */}
              <div className="card-info">
                <p>📍 <strong>City:</strong> {s.city}</p>
                <p>🏠 <strong>Address:</strong> {s.address}</p>
                <p>📞 <strong>Contact:</strong> {s.contact}</p>
              </div>

              {/* ACTIONS */}
              <div className="card-actions">
                <button className="edit-btn" onClick={() => openEdit(s)}>
                  Edit
                </button>

                <button className="delete-btn" onClick={() => deleteItem(s.id)}>
                  Delete
                </button>
              </div>

            </div>
          </div>
        ))}
      </div>

      {/* 🔥 MODAL */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">

            <div className="modal-header">
              <h2>{editMode ? "Update Storage" : "Add Storage"}</h2>
              <span className="close-btn" onClick={() => setShowModal(false)}>✖</span>
            </div>

            <div className="form-group">
              <label>Name</label>
              <input
                value={form.name}
                onChange={e => setForm({ ...form, name: e.target.value })}
              />
              {errors.name && <span className="error">{errors.name}</span>}
            </div>

            <div className="form-group">
              <label>City</label>
              <input
                value={form.city}
                onChange={e => setForm({ ...form, city: e.target.value })}
              />
              {errors.city && <span className="error">{errors.city}</span>}
            </div>

            <div className="form-group">
              <label>Address</label>
              <input
                value={form.address}
                onChange={e => setForm({ ...form, address: e.target.value })}
              />
              {errors.address && <span className="error">{errors.address}</span>}
            </div>

            <div className="form-group">
              <label>Contact</label>
              <input
                value={form.contact}
                onChange={e => setForm({ ...form, contact: e.target.value })}
              />
              {errors.contact && <span className="error">{errors.contact}</span>}
            </div>

            <div className="modal-actions">
              <button onClick={() => setShowModal(false)}>Cancel</button>
              <button className="primary-btn" onClick={submit}>
                {editMode ? "Update" : "Create"}
              </button>
            </div>

          </div>
        </div>
      )}

    </div>
  );
}

/* ================= DONATIONS ================= */
function DonationTable({ showToast }) {
  const [list, setList] = useState([]);
  const [centers, setCenters] = useState([]);
  const [selected, setSelected] = useState({});

  useEffect(() => {
  const fetchData = async () => {
    try {
      const res1 = await axios.get("http://localhost:8080/donation/pending");
      setList(res1.data);

      const res2 = await axios.get("http://localhost:8080/admin/storage-centers");
      setCenters(res2.data);

    } catch (err) {
      showToast("Failed to load data", "error");
    }
  };

  fetchData();
}, []);

 const approve = async (id) => {
  const centerId = selected[id];

  // ❌ VALIDATION
  if (!centerId) {
    showToast("Please select storage center", "error");
    return;
  }

  try {
    const res = await axios.put(
      "http://localhost:8080/admin/donations/approve",
      {
        id,
        storageCenterId: centerId
      }
    );

    showToast(res.data?.message || "Approved successfully");

    // refresh list after approve
    const updated = await axios.get("http://localhost:8080/donation/pending");
    setList(updated.data);

  } catch (err) {
    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "Approval failed";

    showToast(msg, "error");
  }
};

  return (
    <table>
      <thead>
        <tr><th>Name</th><th>Blood</th><th>Action</th></tr>
      </thead>
      <tbody>
        {list.map(d => (
          <tr key={d.id}>
            <td>{d.name}</td>
            <td>{d.bloodGroup}</td>
            <td>
              <select
  value={selected[d.id] || ""}
  onChange={(e) =>
    setSelected({
      ...selected,
      [d.id]: Number(e.target.value)
    })
  }
>
  <option value="">Select Center</option>
  {centers.map((c) => (
    <option key={c.id} value={c.id}>
      {c.name}
    </option>
  ))}
</select>
              <button className="btn btn-success" onClick={()=>approve(d.id)}>Approve</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

/* ================= REQUEST ================= */
function RequestTable({
  showToast,
  centers,
  selectedCenters,
  handleCenterChange,
  fetchPending
}) {
  const [list, setList] = useState([]);

  const fetchData = async () => {
    try {
      const res = await axios.get(
        "http://localhost:8080/blood-request/pending"
      );
      setList(res.data);
    } catch (err) {
      showToast("Failed to load requests", "error");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ✅ APPROVE */
  const approveRequest = async (requestId) => {

  const centerId = selectedCenters[requestId];

  if (!centerId) {
    toast.error("Please select center");
    return;
  }

  try {
    const res = await axios.put(
      "http://localhost:8080/admin/blood-requests/approve",
      {
        requestId: requestId,
        centerId: centerId
      }
    );

    toast.success(res.data.message);

    fetchPending();

  } catch (err) {
    toast.error(err.response?.data?.message || "Failed");
  }
};

  /* ❌ REJECT */
  const reject = async (id) => {
    try {
      const res = await axios.put(
        `http://localhost:8080/admin/blood-requests/${id}/reject`
      );

      showToast(res.data?.message || "Rejected");

      fetchData(); // refresh

    } catch (err) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        "Reject failed";

      showToast(msg, "error");
    }
  };

  return (
    <table>
      <thead>
        <tr>
          <th>Id</th>
          <th>Blood</th>
          <th>Units</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
      </thead>

      <tbody>
  {list.map((r) => (
    <tr key={r.id}>
      <td>{r.id}</td>
      <td>{r.bloodGroup}</td>
      <td>{r.units}</td>
      <td>{r.status}</td>

      {/* 🔥 CENTER DROPDOWN */}
      <td>
  <div className="action-box">
    
    <select
      onChange={(e) =>
        handleCenterChange(r.id, Number(e.target.value))
      }
      disabled={r.status !== "PENDING"}
    >
      <option value="">Select Center</option>
      {centers.map((c) => (
        <option key={c.id} value={c.id}>
          {c.name}
        </option>
      ))}
    </select>

    <button
      className="approve-btn"
      disabled={r.status !== "PENDING"}
      onClick={() => approveRequest(r.id)}
    >
      Approve
    </button>

    <button
      className="reject-btn"
      disabled={r.status !== "PENDING"}
      onClick={() => reject(r.id)}
    >
      Reject
    </button>

  </div>
</td>
    </tr>
  ))}
</tbody>
    </table>
  );
}

/* ================= STOCK ================= */
function StockTable({ refreshTrigger,showToast }) {
  const [centers, setCenters] = useState([]);
  const [stock, setStock] = useState([]);
  const [selectedCenter, setSelectedCenter] = useState(null);

  /* 🔥 FETCH ALL STORAGE CENTERS */
  const fetchCenters = async () => {
    const res = await axios.get("http://localhost:8080/admin/storage-centers");
    setCenters(res.data);
  };

  /* 🔥 FETCH STOCK BY CENTER */
  const fetchStock = async (centerId) => {
  try {
    const res = await axios.get(
      `http://localhost:8080/bloodstock/getbloodstockbycenter/${centerId}`
    );

    setStock(res.data);
    setSelectedCenter(centerId);

  } catch (err) {

    const msg =
      err.response?.data?.message ||
      err.response?.data ||
      "No stock found";

    showToast(msg, "error");   // 🔥 SHOW ERROR

    setStock([]);              // clear old stock
    setSelectedCenter(centerId);
  }
};

  useEffect(() => {
    fetchCenters();
  }, []);

  /* 🔥 AUTO REFRESH WHEN APPROVAL HAPPENS */
  useEffect(() => {
    if (selectedCenter) {
      fetchStock(selectedCenter);
    }
  }, [refreshTrigger]);

  return (
    <div>

      {/* 🔹 STORAGE CENTERS TABLE */}
      <h2>Storage Centers</h2>
      <table>
        <thead>
          <tr>
            <th>Center Name</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {centers.map((c) => (
            <tr key={c.id}>
              <td>{c.name}</td>
              <td>
                <button onClick={() => fetchStock(c.id)}>
                  View Stock
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 🔹 STOCK DISPLAY */}
      {selectedCenter && (
        <div style={{ marginTop: "20px" }}>
          <h3>Blood Stock</h3>

          <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
            {stock.map((s, i) => (
              <div
                key={i}
                style={{
                  border: "1px solid #ccc",
                  padding: "15px",
                  borderRadius: "10px",
                  width: "120px",
                  textAlign: "center",
                  background: "#f9f9f9"
                }}
              >
                <h4>{s.bloodGroup}</h4>
                <p>{s.units} Units</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}



/* ================= USERS ================= */
function UsersTable() {
  const [list, setList] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/admin/users")
      .then(res => setList(res.data));
  }, []);

  return (
    <table>
      <thead>
        <tr><th>Name</th><th>Email</th><th>City</th></tr>
      </thead>
      <tbody>
        {list.map(u => (
          <tr key={u.id}>
            <td>{u.name}</td>
            <td>{u.email}</td>
            <td>{u.city}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

/* ================= CARD ================= */
function Card({ title, value }) {
  return (
    <div className="card">
      <h3>{title}</h3>
      <h1>{value}</h1>
    </div>
  );
}