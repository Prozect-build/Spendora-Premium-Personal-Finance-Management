import { Injectable, signal, computed } from '@angular/core';
import {
  Expense, Category, Budget,
  MonthlySummary, CategoryBreakdown, Page, ExpenseFilter
} from '../models/expense.model';

/**
 * ExpenseService — manages all expense/budget data using Signals.
 * Currently uses rich mock data so the UI is fully interactive.
 * Every method is designed to be swapped with real HttpClient calls later.
 */
@Injectable({ providedIn: 'root' })
export class ExpenseService {

  // ── Mock Categories ───────────────────────────────────────
  private mockCategories: Category[] = [
    { id: 1, name: 'Food & Dining',   icon: 'restaurant',     color: '#F59E0B', type: 'EXPENSE' },
    { id: 2, name: 'Transport',       icon: 'directions_car', color: '#3B82F6', type: 'EXPENSE' },
    { id: 3, name: 'Shopping',        icon: 'shopping_bag',   color: '#EC4899', type: 'EXPENSE' },
    { id: 4, name: 'Entertainment',   icon: 'movie',          color: '#8B5CF6', type: 'EXPENSE' },
    { id: 5, name: 'Bills & Utilities',icon: 'receipt',       color: '#EF4444', type: 'EXPENSE' },
    { id: 6, name: 'Health',          icon: 'favorite',       color: '#10B981', type: 'EXPENSE' },
    { id: 7, name: 'Education',       icon: 'school',         color: '#06B6D4', type: 'EXPENSE' },
    { id: 8, name: 'Salary',          icon: 'account_balance_wallet', color: '#10B981', type: 'INCOME' },
    { id: 9, name: 'Freelance',       icon: 'laptop',         color: '#7C6FFF', type: 'INCOME' },
    { id: 10, name: 'Investments',    icon: 'trending_up',    color: '#F59E0B', type: 'INCOME' },
  ];

  // ── Mock Expenses ─────────────────────────────────────────
  private mockExpenses: Expense[] = [
    { id: 1,  title: 'Lunch at Cafe Coffee Day', amount: 450,  category: this.mockCategories[0], date: '2025-02-22', type: 'EXPENSE', note: 'With team' },
    { id: 2,  title: 'Uber to Office',           amount: 220,  category: this.mockCategories[1], date: '2025-02-22', type: 'EXPENSE' },
    { id: 3,  title: 'Monthly Salary',            amount: 75000, category: this.mockCategories[7], date: '2025-02-01', type: 'INCOME' },
    { id: 4,  title: 'Amazon Shopping',           amount: 2399, category: this.mockCategories[2], date: '2025-02-20', type: 'EXPENSE', note: 'Headphones' },
    { id: 5,  title: 'Netflix Subscription',      amount: 499,  category: this.mockCategories[3], date: '2025-02-18', type: 'EXPENSE' },
    { id: 6,  title: 'Electricity Bill',          amount: 1200, category: this.mockCategories[4], date: '2025-02-15', type: 'EXPENSE' },
    { id: 7,  title: 'Doctor Consultation',       amount: 800,  category: this.mockCategories[5], date: '2025-02-14', type: 'EXPENSE' },
    { id: 8,  title: 'Freelance Project',         amount: 15000, category: this.mockCategories[8], date: '2025-02-10', type: 'INCOME' },
    { id: 9,  title: 'Grocery Shopping',          amount: 1850, category: this.mockCategories[0], date: '2025-02-08', type: 'EXPENSE' },
    { id: 10, title: 'Auto Rickshaw',             amount: 85,   category: this.mockCategories[1], date: '2025-02-07', type: 'EXPENSE' },
    { id: 11, title: 'Udemy Course',              amount: 599,  category: this.mockCategories[6], date: '2025-02-05', type: 'EXPENSE' },
    { id: 12, title: 'Dinner at Barbeque Nation', amount: 1250, category: this.mockCategories[0], date: '2025-02-03', type: 'EXPENSE' },
  ];

  // ── Reactive Signals ─────────────────────────────────────
  expenses  = signal<Expense[]>(this.mockExpenses);
  categories = signal<Category[]>(this.mockCategories);

  // ── Computed summary for this month ───────────────────────
  get totalIncome():  number { return this.expenses().filter(e => e.type === 'INCOME').reduce((s, e) => s + e.amount, 0); }
  get totalExpense(): number { return this.expenses().filter(e => e.type === 'EXPENSE').reduce((s, e) => s + e.amount, 0); }
  get netBalance():   number { return this.totalIncome - this.totalExpense; }

  // ── CRUD (mock, returns Promise to match future HTTP shape) ─

  getExpenses(filter?: ExpenseFilter): Promise<Page<Expense>> {
    let data = [...this.expenses()];
    if (filter?.type && filter.type !== 'ALL') {
      data = data.filter(e => e.type === filter.type);
    }
    data.sort((a, b) => b.date.localeCompare(a.date));
    return Promise.resolve({ content: data, totalElements: data.length, totalPages: 1, number: 0, size: data.length });
  }

  addExpense(expense: Omit<Expense, 'id' | 'createdAt'>): Promise<Expense> {
    const newExpense: Expense = { ...expense, id: Date.now(), createdAt: new Date().toISOString() };
    this.expenses.update(prev => [newExpense, ...prev]);
    return Promise.resolve(newExpense);
  }

  updateExpense(id: number, updates: Partial<Expense>): Promise<Expense> {
    let updated!: Expense;
    this.expenses.update(prev => prev.map(e => {
      if (e.id === id) { updated = { ...e, ...updates }; return updated; }
      return e;
    }));
    return Promise.resolve(updated);
  }

  deleteExpense(id: number): Promise<void> {
    this.expenses.update(prev => prev.filter(e => e.id !== id));
    return Promise.resolve();
  }

  getCategories(): Promise<Category[]> {
    return Promise.resolve(this.categories());
  }

  // ── Budgets (Reactive) ───────────────────────────────────
  private initialBudgets: Budget[] = [
    { id: 1, category: this.mockCategories[0], limitAmount: 5000,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 2, category: this.mockCategories[1], limitAmount: 2000,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 3, category: this.mockCategories[2], limitAmount: 3000,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 4, category: this.mockCategories[3], limitAmount: 1000,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 5, category: this.mockCategories[4], limitAmount: 2500,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 6, category: this.mockCategories[5], limitAmount: 1500,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
    { id: 7, category: this.mockCategories[6], limitAmount: 1000,  spentAmount: 0, month: 2, year: 2025, percentage: 0 },
  ];

  budgets = computed(() => {
    const exps = this.expenses();
    return this.initialBudgets.map(b => {
      const spent = exps
        .filter(e => e.category.id === b.category.id && e.type === 'EXPENSE')
        .reduce((s, e) => s + e.amount, 0);
      return {
        ...b,
        spentAmount: spent,
        percentage: Math.round((spent / b.limitAmount) * 100)
      };
    });
  });

  async getBudgets(): Promise<Budget[]> {
    return this.budgets();
  }

  // ── Monthly Summary ───────────────────────────────────────
  getMonthlySummary(): MonthlySummary {
    const expenses = this.expenses();
    const currentBudgets = this.initialBudgets; // In reality, this would be fetched
    
    const breakdown: CategoryBreakdown[] = this.mockCategories
      .filter(c => c.type === 'EXPENSE')
      .map(cat => {
        const total = expenses
          .filter(e => e.category.id === cat.id && e.type === 'EXPENSE')
          .reduce((s, e) => s + e.amount, 0);
        return {
          category: cat,
          totalAmount: total,
          transactionCount: expenses.filter(e => e.category.id === cat.id).length,
          percentage: this.totalExpense > 0 ? Math.round((total / this.totalExpense) * 100) : 0,
        };
      })
      .filter(b => b.totalAmount > 0)
      .sort((a, b) => b.totalAmount - a.totalAmount);

    return {
      month: 2, year: 2025,
      totalIncome: this.totalIncome,
      totalExpense: this.totalExpense,
      netBalance: this.netBalance,
      categoryBreakdown: breakdown,
    };
  }
}
