import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ExpenseService } from '../../core/services/expense.service';
import { AuthService } from '../../core/services/auth.service';
import { Budget } from '../../core/models/expense.model';

@Component({
  selector: 'app-budgets',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './budgets.component.html',
  styleUrl: './budgets.component.css',
})
export class BudgetsComponent implements OnInit {
  budgets   = signal<Budget[]>([]);
  isLoading = signal(true);
  protected readonly Math = Math;

  constructor(
    private expenseService: ExpenseService,
    private auth: AuthService,
    private router: Router,
  ) {}

  async ngOnInit() {
    this.budgets.set(await this.expenseService.getBudgets());
    this.isLoading.set(false);
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(amount);
  }

  getBudgetColor(pct: number): string {
    if (pct >= 90) return 'var(--color-expense)';
    if (pct >= 70) return 'var(--color-warning)';
    return 'var(--color-income)';
  }

  navigateTo(path: string) { this.router.navigate([path]); }

  get totalAllocated(): number { return this.budgets().reduce((s, b) => s + b.limitAmount, 0); }
  get totalSpent():     number { return this.budgets().reduce((s, b) => s + b.spentAmount, 0); }
  get overBudget():     number { return this.budgets().filter(b => b.percentage >= 100).length; }
}
