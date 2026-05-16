import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "../styles/Register.css";

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    bloodGroup: "",
    role: "",
    city: "",
    image: null,
  });

  const [errors, setErrors] = useState({});

  // ✅ Validation function
  const validate = () => {
    let newErrors = {};

    if (!formData.name || formData.name.length < 2) {
      newErrors.name = "Name must be at least 2 characters";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email || !emailRegex.test(formData.email)) {
      newErrors.email = "Enter a valid email";
    }

    if (!formData.password || formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    if (!formData.bloodGroup) {
      newErrors.bloodGroup = "Blood group is required";
    }

    if (!formData.city) {
      newErrors.city = "City is required";
    }

    if (!formData.role) {
      newErrors.role = "Role is required";
    }

    if (!formData.image) {
      newErrors.image = "Image is required";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  // ✅ Handle Input
  const handleChange = (e) => {
    const { name, value, files } = e.target;

    if (name === "image") {
      setFormData({ ...formData, image: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  // ✅ Submit
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) {
      toast.error("Please fix form errors");
      return;
    }

    try {
      const data = new FormData();

      data.append("name", formData.name);
      data.append("email", formData.email);
      data.append("password", formData.password);
      data.append("bloodGroup", formData.bloodGroup);
      data.append("role", formData.role);
      data.append("city", formData.city);
      data.append("image", formData.image);

      await axios.post("http://localhost:8080/user/register", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      toast.success("🎉 Registration successful!");

      setTimeout(() => navigate("/login"), 1500);

    } catch (err) {
      toast.error(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2>Register</h2>

        <form onSubmit={handleSubmit}>

          {/* NAME */}
          <input
            type="text"
            name="name"
            placeholder="Full Name"
            onChange={handleChange}
          />
          <p className="error">{errors.name}</p>

          {/* EMAIL */}
          <input
            type="email"
            name="email"
            placeholder="Email"
            onChange={handleChange}
          />
          <p className="error">{errors.email}</p>

          {/* PASSWORD */}
          <input
            type="password"
            name="password"
            placeholder="Password"
            onChange={handleChange}
          />
          <p className="error">{errors.password}</p>

          {/* BLOOD GROUP */}
          <select name="bloodGroup" onChange={handleChange}>
            <option value="">Select Blood Group</option>
            <option value="A+">A+</option>
            <option value="B+">B+</option>
            <option value="O+">O+</option>
            <option value="AB+">AB+</option>
          </select>
          <p className="error">{errors.bloodGroup}</p>

          {/* CITY */}
          <input
            type="text"
            name="city"
            placeholder="City"
            onChange={handleChange}
          />
          <p className="error">{errors.city}</p>

          {/* IMAGE */}
          <input
            type="file"
            name="image"
            accept="image/*"
            onChange={handleChange}
          />
          <p className="error">{errors.image}</p>

          {/* ROLE */}
          <div className="role-selection">
            <label>
              <input type="radio" name="role" value="DONOR" onChange={handleChange} />
              Donor
            </label>

            <label>
              <input type="radio" name="role" value="HOSPITAL" onChange={handleChange} />
              Hospital
            </label>
          </div>
          <p className="error">{errors.role}</p>

          <button type="submit">Register</button>

        </form>
      </div>
    </div>
  );
}

export default Register;