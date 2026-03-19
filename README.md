# 💻 Spendora Frontend – Modern FinTech UI

The Spendora Frontend is a high-performance web application built with **Angular 19+**. It features a modern, professional, and visually premium interface designed for personal finance management.

---

## ✨ Features

- **Dashboard:** Interactive cards for balance monitoring and monthly spending overviews.
- **Analytics:** Data-driven visualizations using charting libraries.
- **Expenses & Budgets:** Clean tabular views and forms for financial entry.
- **Auth Flow:** Seamless login and registration with OTP (One-Time Password) support.
- **Glassmorphism:** Elegant use of transparency and soft shadows for a high-end software feel.

---

## 🏗️ Architecture

The app follows a modular and scalable structure:

- **`core/`**: Centralized services, guards, and interceptors (e.g., Auth, Error handling).
- **`features/`**: Feature-specific modules (Dashboard, Expenses, Budgets, Auth).
- **`shared/`**: Reusable UI components (Buttons, Inputs, Modals, Cards).
- **`layouts/`**: Overall page layouts (Sidebar, Header, Dual-Sidebar).

---

## 🛠️ Development

### Setup
Ensure you have [Node.js](https://nodejs.org/) and [Angular CLI](https://github.com/angular/angular-cli) installed.

1. Clone the repository.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   ng serve
   ```
4. Access the app at `http://localhost:4200/`.

---

## 🎨 Design System

Spendora follows a strict design system:
- **Primary Color:** Emerald Green (#10b981)
- **Background:** Minimalist light/dark theme with glass effects.
- **Typography:** Inter or Poppins for clarity and a modern feel.
- **Icons:** Lucide or Heroicons for consistency.

---

## 🚦 Routing

Key routes in the application:
- `/dashboard`: Main financial overview.
- `/expenses`: Transaction history and tracking.
- `/budgets`: Category-based budget planning.
- `/auth/login`: User authentication.

---

## 📦 Building

To generate a production build:
```bash
ng build --configuration production
```
The output will be in the `dist/` folder.
