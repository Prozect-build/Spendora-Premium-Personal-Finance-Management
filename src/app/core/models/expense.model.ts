// ── Expense Model ────────────────────────────────────────
export interface Expense {
  id: number;
  title: string;
  amount: number;           // Maps to BigDecimal from backend
  category: Category;
  date: string;             // ISO date string (yyyy-MM-dd)
  note?: string;
  type: 'EXPENSE' | 'INCOME';
  createdAt?: string;
}

// ── Category Model ───────────────────────────────────────
export interface Category {
  id: number;
  name: string;
  icon: string;             // Material Icon name
  color: string;            // Hex color
  type: 'EXPENSE' | 'INCOME' | 'BOTH';
}

// ── Budget Model ─────────────────────────────────────────
export interface Budget {
  id: number;
  category: Category;
  limitAmount: number;      // BigDecimal from backend
  spentAmount: number;
  month: number;            // 1–12
  year: number;
  percentage: number;       // Computed: spentAmount / limitAmount * 100
}

// ── Summary / Analytics ──────────────────────────────────
export interface MonthlySummary {
  month: number;
  year: number;
  totalIncome: number;
  totalExpense: number;
  netBalance: number;
  categoryBreakdown: CategoryBreakdown[];
}

export interface CategoryBreakdown {
  category: Category;
  totalAmount: number;
  percentage: number;
  transactionCount: number;
}

// ── User Model ───────────────────────────────────────────
export interface User {
  id: number;
  name: string;
  email: string;
  createdAt?: string;
}

// ── Auth Models ──────────────────────────────────────────
export interface SendOtpRequest {
  email: string;
}

export interface VerifyOtpRequest {
  email: string;
  otp: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

// ── API Envelope (matches backend ApiResponse<T>) ─────────
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
}

// ── Expense Filter Options ────────────────────────────────
export interface ExpenseFilter {
  type?: 'EXPENSE' | 'INCOME' | 'ALL';
  categoryId?: number;
  startDate?: string;
  endDate?: string;
  month?: number;
  year?: number;
}

// ── Paginated Response ────────────────────────────────────
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
