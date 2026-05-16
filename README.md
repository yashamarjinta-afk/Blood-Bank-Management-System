# 🩸 Blood Bank Management System

A full-stack **Blood Bank Management System** developed to simplify blood donation, donor registration, blood request handling, and inventory management. This project helps hospitals, donors, and administrators efficiently manage blood availability and requests in real time.

---

# 📌 Overview

The Blood Bank Management System is designed to digitize and automate the process of managing blood donations and requests. The system allows users to register as donors, search for blood groups, request blood, and manage inventory through an admin dashboard.

This project improves efficiency, reduces manual work, and ensures quick access to blood availability information during emergencies.

---

# ✨ Features

## 👤 User Module
- User Registration & Login
- Secure Authentication
- Update Profile Information
- Search Blood by Group

## 🩸 Donor Module
- Donor Registration
- Blood Donation Records
- Availability Status Management
- Donor Details Management

## 🏥 Admin Module
- Manage Users & Donors
- Approve/Reject Blood Requests
- Maintain Blood Stock
- Dashboard with Statistics
- Monitor Blood Availability

## 📦 Blood Inventory
- Add Blood Units
- Update Stock Quantity
- Track Blood Groups
- Low Stock Alerts

## 📋 Request Management
- Raise Blood Requests
- View Request Status
- Approve or Reject Requests
- Emergency Blood Requests

---

# 🛠️ Tech Stack

## Frontend
- HTML5
- CSS3
- JavaScript
- Bootstrap

## Backend
- Java / Spring Boot

## Database
- MySQL

## Tools & Platforms
- Git & GitHub
- VS Code / IntelliJ IDEA
- Postman
- MySQL Workbench

---

# 📂 Project Structure

```bash
BloodBankManagementSystem/
│
├── frontend/
│   ├── html/
│   ├── css/
│   ├── js/
│   └── assets/
│
├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/
│   └── config/
│
├── database/
│   └── bloodbank.sql
│
├── screenshots/
│
└── README.md
```

---

# 🚀 Installation & Setup

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/blood-bank-management-system.git
```

---

## 2️⃣ Navigate to Project Directory

```bash
cd blood-bank-management-system
```

---

## 3️⃣ Configure Database

### Create Database

```sql
CREATE DATABASE bloodbank;
```

### Import SQL File

Import the `bloodbank.sql` file into MySQL using MySQL Workbench or command line.

---

## 4️⃣ Update Database Configuration

Update your database credentials inside:

```bash
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bloodbank
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

## 5️⃣ Run Backend Server

```bash
mvn spring-boot:run
```

---

## 6️⃣ Run Frontend

Open `index.html` in your browser.

---

# 🔐 Authentication & Security

- Secure Login System
- Session Management
- Password Encryption
- Role-Based Access Control
- Admin Authorization

---

# 📊 Dashboard Functionalities

- Total Donors Count
- Available Blood Units
- Pending Requests
- Approved Requests
- Blood Group Statistics

---

# 💻 Screenshots



```md
![Home Page](screenshots/home.png)

![Dashboard](screenshots/dashboard.png)

![Blood Request](screenshots/request.png)

![Admin Panel](screenshots/admin.png)
```

---

# 📈 Future Enhancements

- Email Notifications
- SMS Alerts
- Online Appointment Booking
- AI-based Blood Demand Prediction
- Location-Based Blood Search
- Mobile Application
- Cloud Deployment

---

# 🤝 Contribution

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to GitHub
5. Open a Pull Request

---

# 📄 License

This project is developed for educational and learning purposes.

---

# 👨‍💻 Author

## Yash Amarjinta

- GitHub: https://github.com/yashamarjinta-afk
- LinkedIn: https://linkedin.com/in/yashamarjinta

---

# ⭐ Support

If you found this project useful, please give it a ⭐ on GitHub.
