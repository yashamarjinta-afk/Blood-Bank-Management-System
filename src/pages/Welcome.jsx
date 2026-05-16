import Navbar from "../components/Navbar.jsx";
import "../styles/welcome.css";
import { Link } from "react-router-dom";

const Welcome = () => {
  return (
    <>
     

      {/* HERO SECTION */}
      <section className="hero">
        <div className="hero-content">
          <h1>Donate Blood. Save Lives.</h1>
          <p>
            A smart blood bank management system connecting donors and
            patients instantly during emergencies.
          </p>

          <div className="hero-buttons">
            <Link to="/register">
              <button className="primary-btn">Become a Donor</button>
            </Link>
            <Link to="/login">
              <button className="secondary-btn">Request Blood</button>
            </Link>
          </div>
        </div>
      </section>

      {/* FEATURES SECTION */}
      <section className="features">
        <h2>Why Choose Us?</h2>
        <div className="feature-grid">
          <div className="feature-card">
            <h3>⚡ Fast Requests</h3>
            <p>Quick blood request processing during emergencies.</p>
          </div>

          <div className="feature-card">
            <h3>📊 Real-Time Stock</h3>
            <p>Live tracking of available blood units.</p>
          </div>

          <div className="feature-card">
            <h3>🔔 Instant Alerts</h3>
            <p>Email notifications for approvals and urgent cases.</p>
          </div>
        </div>
      </section>

      {/* STATS SECTION */}
      <section className="stats">
        <div className="stat">
          <h3>500+</h3>
          <p>Registered Donors</p>
        </div>

        <div className="stat">
          <h3>1200+</h3>
          <p>Successful Donations</p>
        </div>

        <div className="stat">
          <h3>24/7</h3>
          <p>Emergency Support</p>
        </div>
      </section>

      {/* CTA SECTION */}
      <section className="cta">
        <h2>Be a Hero Today</h2>
        <p>Your one donation can save multiple lives.</p>
        <Link to="/register">
          <button className="primary-btn">Join Now</button>
        </Link>
      </section>

      
    </>
  );
};

export default Welcome;