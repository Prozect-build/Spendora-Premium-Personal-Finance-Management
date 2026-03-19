# 💰 Spendora – Premium Personal Finance Management

Spendora is a modern, high-end personal finance management application designed to help users track expenses, manage budgets, and visualize their financial health with a professional, FinTech-inspired interface.

Built with a **Spring Boot** backend and an **Angular** frontend, Spendora offers a secure and intuitive platform for taking control of your financial journey.

---

## 🚀 Key Features

- **📊 Comprehensive Dashboard:** Get a high-level overview of your balances, recent transactions, and budget status at a glance.
- **💸 Expense Tracking:** Effortlessly record and categorize your spending to identify habits and save more.
- **🛡️ Secure Authentication:** Robust security powered by JWT and **2FA via OTP** (Email) for extra peace of mind.
- **📊 Real-time Analytics:** Advanced data visualization and insights to help you understand where your money goes.
- **📅 Budget Management:** Set and monitor budgets across various categories to prevent overspending.
- **📱 Premium UI/UX:** A sleek, green-themed design optimized for both desktop and mobile use.

---

## 🛠️ Technology Stack

### Backend
- **Framework:** [Spring Boot](https://spring.io/projects/spring-boot) (Java 17+)
- **Security:** Spring Security with JWT & Email OTP Verification
- **Database:** MySQL
- **Build Tool:** Maven

### Frontend
- **Framework:** [Angular 19+](https://angular.dev/)
- **UI Components:** Tailored custom components with a professional FinTech aesthetic
- **Styling:** Modern CSS/Tailwind (optimized for layout & micro-interactions)

---

## 📁 Project Structure

```text
Spendora/
├── spendora-backend/      # Spring Boot application
│   ├── src/main/java/     # Controllers, Services, Models, Security
│   ├── src/main/resources/ # Application properties & Configuration
│   └── pom.xml            # Maven dependencies
└── spendora-frontend/     # Angular web application
    ├── src/app/features/  # Core feature modules (Dashboard, Auth, etc.)
    ├── src/app/shared/    # Reusable components & utilities
    └── angular.json       # Project configuration
```

---

## 🛠️ Getting Started

### 1. Backend Setup
1. Navigate to `spendora-backend/`.
2. Configure your database in `src/main/resources/application.properties`.
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### 2. Frontend Setup
1. Navigate to `spendora-frontend/`.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   ng serve
   ```
4. Open your browser and go to `http://localhost:4200/`.

---

## ✨ Design Philosophy
Spendora follows a **professional green-based color theme**, emphasizing clarity, trust, and premium aesthetics. The UI incorporates:
- Neumorphic and glassmorphic elements for depth.
- Smooth transitions and hover effects for enhanced interaction.
- A monochromatic and clean grid layout for data visualization.

---

## 🤝 Contributing
Contributions are welcome! If you're interested in improving Spendora, feel free to fork the repository and submit a pull request.

## 📝 License
This project is licensed under the MIT License.
