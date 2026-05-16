import Navbar from "../components/Navbar";
import "../styles/about.css";

const About = () => {
  return (
    <>
      

      <section className="about-container">
        <div className="about-content">
          <h1>About BloodBank</h1>

          <p>
            BloodBank is a smart blood bank management system designed to
            connect donors and patients quickly and efficiently during
            emergencies.
          </p>

          <p>
            Our platform allows users to register as donors, request blood,
            and manage blood availability seamlessly. We aim to reduce delays
            and save lives by providing a reliable digital solution.
          </p>

          <div className="about-features">
            <div className="feature-card">
              <h3>Fast Response</h3>
              <p>Instant connection between donors and patients.</p>
            </div>

            <div className="feature-card">
              <h3>Secure System</h3>
              <p>Protected user data with secure authentication.</p>
            </div>

            <div className="feature-card">
              <h3>Easy to Use</h3>
              <p>Simple and user-friendly interface.</p>
            </div>
          </div>
        </div>
      </section>
      {/* FOOTER */}
      <footer className="footer">
        <p>© 2026 Blood Bank Management System | Built with ❤️</p>
      </footer>
    </>
  );
};

export default About;