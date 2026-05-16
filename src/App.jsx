import { Routes, Route, useLocation } from "react-router-dom";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import Welcome from "./pages/Welcome";
import About from "./pages/About";
import Contact from "./pages/Contactus";
import Register from "./pages/Register";
import Login from "./pages/Login";
import AdminDashboard from "./pages/AdminDashboard";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import DonorDashboard from "./pages/DonorDashboard";
import OrganizerDashboard from "./pages/OrganizerDashboard";
import HospitalDashboard from "./pages/HospitalDashboard";

function App() {
 
  const location = useLocation();

  // ✅ Check if admin OR donor page
  const isDashboard =
    location.pathname.startsWith("/admin") ||
    location.pathname.startsWith("/donor") || location.pathname.startsWith("/organizer") || location.pathname.startsWith("/hospital");

  return (
    <>
      <ToastContainer position="top-right" autoClose={2000} />

      <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>

        {/* ✅ Hide Navbar for Admin + Donor */}
        {!isDashboard && <Navbar />}

        <div style={{ flex: 1 }}>
          <Routes>
            <Route path="/" element={<Welcome />} />
            <Route path="/about" element={<About />} />
            <Route path="/contact" element={<Contact />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />

            {/* ✅ Dashboards */}
            <Route path="/admin-dashboard" element={<AdminDashboard />} />
            <Route path="/donor-dashboard" element={<DonorDashboard />} />
            <Route path="/organizer-dashboard"element={<OrganizerDashboard/>}/>
            <Route path="/hospital-dashboard" element={<HospitalDashboard/>}/>
          </Routes>
        </div>

        {/* ✅ Optional: Hide Footer also for dashboards */}
      <Footer />

      </div>
    </>
  );

}

export default App;