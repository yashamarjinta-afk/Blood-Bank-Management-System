import Navbar from "../components/Navbar";
import "../styles/Contact.css";

function Contact() {
  return (
    
    <div className="contact-container">
      <div className="contact-card">
        <h1>Contact Us</h1>
        <p>We'd love to hear from you.</p>

        <form className="contact-form">
          <input type="text" placeholder="Your Name" />
          <input type="email" placeholder="Your Email" />
          <textarea rows="5" placeholder="Your Message"></textarea>
          <button type="submit">Send Message</button>
        </form>
      </div>
    </div>
  );
}

export default Contact;