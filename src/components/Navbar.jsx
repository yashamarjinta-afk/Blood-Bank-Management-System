 import { Link } from "react-router-dom";
import "../styles/navbar.css";

const Navbar = () => {
  return (
    <header className="navbar">
      <div className="nav-container">
        
        {/* Logo */}
        <div className="logo">
          🩸 RedCross
        </div>

        {/* Navigation Links */}
        <nav className="nav-links">
          <Link to="/">Home</Link>
          <Link to="/about">About Us</Link>
          <Link to="/contact">Contact Us</Link>
          <Link to="/login" className="login-link">Login</Link>
          <Link to="/register" className="register-btn">Register</Link>
        </nav>

      </div>
    </header>
  );
};

export default Navbar;