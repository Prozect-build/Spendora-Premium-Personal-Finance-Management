import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css'
})
export class AnalyticsComponent {
  // Mock data for analytics
  totalBalance = signal(12540.50);
  monthlyIncome = signal(8500.00);
  monthlyExpense = signal(3240.25);
  savingsRate = signal(62);

  spendingCategories = signal([
    { name: 'Food', amount: 850.00, color: '#FF5722', percentage: 26 },
    { name: 'Shopping', amount: 1200.00, color: '#E91E63', percentage: 37 },
    { name: 'Transport', amount: 450.00, color: '#2196F3', percentage: 14 },
    { name: 'Rent', amount: 500.00, color: '#795548', percentage: 15 },
    { name: 'Entertainment', amount: 240.25, color: '#9C27B0', percentage: 8 }
  ]);

  recentTrends = signal([
    { month: 'Jan', income: 8000, expense: 3500 },
    { month: 'Feb', income: 8200, expense: 3100 },
    { month: 'Mar', income: 8500, expense: 3240 }
  ]);
}
