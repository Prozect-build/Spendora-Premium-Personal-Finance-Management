import { Component, signal, computed, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ExpenseService } from '../../core/services/expense.service';
import { Expense, MonthlySummary, CategoryBreakdown } from '../../core/models/expense.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {

  summary     = signal<MonthlySummary | null>(null);
  recentTxns  = signal<Expense[]>([]);
  isLoading   = signal(true);

  protected readonly Math = Math;

  // For the donut chart (mock ring segments)
  ringSegments = computed(() => {
    const breakdown = this.summary()?.categoryBreakdown ?? [];
    const colors    = breakdown.map((b: CategoryBreakdown) => b.category.color);
    const percents  = breakdown.map((b: CategoryBreakdown) => b.percentage);
    return { colors, percents };
  });

  unbudgetedTotal = computed(() => {
    const expenses = this.expense.expenses();
    const budgets  = this.expense.budgets();
    const budgetedCatIds = new Set(budgets.map(b => b.category.id));
    
    return expenses
      .filter(e => e.type === 'EXPENSE' && !budgetedCatIds.has(e.category.id))
      .reduce((s, e) => s + e.amount, 0);
  });

  constructor(
    readonly auth: AuthService,
    readonly expense: ExpenseService,
    private router: Router,
  ) {}

  async ngOnInit() {
    this.isLoading.set(true);
    const [, page] = await Promise.all([
      Promise.resolve(),
      this.expense.getExpenses({ type: 'ALL' }),
    ]);
    this.summary.set(this.expense.getMonthlySummary());
    this.recentTxns.set(page.content.slice(0, 6));
    this.isLoading.set(false);
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 0,
    }).format(amount);
  }

  getGreeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Good morning';
    if (h < 17) return 'Good afternoon';
    return 'Good evening';
  }

  navigateTo(path: string) {
    this.router.navigate([path]);
  }

  getBudgetColor(percentage: number): string {
    if (percentage >= 90) return 'var(--color-expense)';
    if (percentage >= 70) return 'var(--color-warning)';
    return 'var(--color-income)';
  }

  // Returns the CSS conic-gradient for mini donut chart
  getDonutGradient(): string {
    const bd = this.summary()?.categoryBreakdown ?? [];
    let cumulative = 0;
    const segments = bd.map((b: CategoryBreakdown) => {
      const start = cumulative;
      cumulative += b.percentage;
      return `${b.category.color} ${start}% ${cumulative}%`;
    });
    return segments.length
      ? `conic-gradient(${segments.join(', ')})`
      : `conic-gradient(var(--border-muted) 0% 100%)`;
  }
}
