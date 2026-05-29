import { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { motion } from "framer-motion";
import { Oval } from "react-loader-spinner";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";

const Login = () => {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate=useNavigate();
 

  const [showModal, setShowModal] = useState(false);
  const [step, setStep] = useState(1);

  const [resetEmail, setResetEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");

  const [loading, setLoading] = useState(false);

  const API = "https://blood-bank-management-system-production-11a1.up.railway.app";

  // ================= LOGIN =================
 const handleLogin = async (e) => {
  e.preventDefault();

  if (!email || !password) {
    toast.error("Email & Password required ❌");
    return;
  }

  setLoading(true);

  try {
    // ✅ create formdata here
    const formData = new FormData();
    formData.append("email", email);
    formData.append("password", password);

   const response = await axios.post(
  `${API}/user/login`,
  formData,
  {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  }
);

    const user = response.data;

    // ✅ store user
    localStorage.setItem("user", JSON.stringify(user));

    if (user.message === "Login successful") {
      toast.success("Login successful! ✅");

      // ✅ handle role (string or array)
      let role = user.role;

      if (Array.isArray(role)) {
        role = role[0];
      }

      role = role?.replace("ROLE_", "").toUpperCase();

      // ✅ navigation
      if (role === "ADMIN") {
        navigate("/admin-dashboard");
      } else if (role === "HOSPITAL") {
        navigate("/hospital-dashboard");
      } else if (role === "ORGANIZER") {
        navigate("/organizer-dashboard");
      } else if (role === "DONOR") {
        navigate("/donor-dashboard");
      } else {
        navigate("/");
      }

    } else {
      toast.error(user.msg || "Login failed ❌");
    }

  } catch (error) {
    toast.error(error.response?.data?.msg || "Login failed...");
  } finally {
    setLoading(false);
  }
};

  // ================= STEP 1 =================
 const sendOtp = async () => {

  if (!resetEmail) {
    toast.error("Email is required ❌");
    return;
  }

  setLoading(true);

  try {
    const res = await axios.post(`${API}/user/forgot-password?email=${resetEmail}`);

    toast.success(res.data); // "OTP sent successfully"
    setStep(2);

  } catch (err) {
    toast.error(err.response?.data || "Failed to send OTP");
  } finally {
    setLoading(false);
  }
};

  // ================= STEP 2 =================
  const verifyOtp = async () => {
    setLoading(true);
    try {
      await axios.post(`${API}/user/verify-otp?email=${resetEmail}&otp=${parseInt(otp)}`);
      toast.success("OTP verified ✅");
      setStep(3);
    } catch {
      toast.error("Invalid OTP ❌");
    } finally {
      setLoading(false);
    }
  };

  // ================= STEP 3 =================
  const resetPassword = async () => {
    setLoading(true);
    try {
      await axios.post(`${API}/user/reset-password?email=${resetEmail}&newpassword=${newPassword}`);

      toast.success("Password reset 🎉");
      setShowModal(false);
      setStep(1);
    } catch {
      toast.error("Reset failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">

      {/* LOGIN */}
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Login</h2>

        <input
          type="email"
          placeholder="Enter Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Enter Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit" disabled={loading}>
          {loading ? <Oval height={20} width={20} /> : "Login"}
        </button>

        <p onClick={() => setShowModal(true)} className="forgot-link">
          Forgot Password?
        </p>
      </form>

      {/* ================= MODAL ================= */}
      {showModal && (
        <div className="modal-overlay">

          <motion.div
            className="modal-box"
            initial={{ scale: 0.7, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ duration: 0.3 }}
          >
            <h3>Reset Password</h3>

            {/* STEP INDICATOR */}
            <div className="steps">
              {[1,2,3].map((s) => (
                <div key={s} className={step >= s ? "active step" : "step"}>
                  {s}
                </div>
              ))}
            </div>

            {/* STEP CONTENT WITH ANIMATION */}
            <motion.div
              key={step}
              initial={{ x: 50, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: -50, opacity: 0 }}
            >

              {step === 1 && (
                <>
                  <input
                    placeholder="Enter Email"
                    value={resetEmail}
                    onChange={(e) => setResetEmail(e.target.value)}
                  />
                  <button onClick={sendOtp}>
                    {loading ? <Oval height={20} width={20}/> : "Send OTP"}
                  </button>
                </>
              )}

              {step === 2 && (
                <>
                  <input
                    placeholder="Enter OTP"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                  />
                  <button onClick={verifyOtp}>
                    {loading ? <Oval height={20} width={20}/> : "Verify OTP"}
                  </button>
                </>
              )}

              {step === 3 && (
                <>
                  <input
                    type="password"
                    placeholder="New Password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                  <button onClick={resetPassword}>
                    {loading ? <Oval height={20} width={20}/> : "Reset Password"}
                  </button>
                </>
              )}

            </motion.div>

            <button
              className="close-btn"
              onClick={() => {
                setShowModal(false);
                setStep(1);
              }}
            >
              Close
            </button>

          </motion.div>
        </div>
      )}

    </div>
  );
};

export default Login;