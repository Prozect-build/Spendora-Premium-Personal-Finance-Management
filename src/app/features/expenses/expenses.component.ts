import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ExpenseService } from '../../core/services/expense.service';
import { AuthService } from '../../core/services/auth.service';
import { Expense, Category } from '../../core/models/expense.model';

@Component({
  selector: 'app-expenses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './expenses.component.html',
  styleUrl: './expenses.component.css',
})
export class ExpensesComponent implements OnInit {
  expenses    = signal<Expense[]>([]);
  categories  = signal<Category[]>([]);
  isLoading   = signal(true);
  showModal   = signal(false);
  filterType  = signal<'ALL' | 'EXPENSE' | 'INCOME'>('ALL');

  // New expense form state
  form = signal({
    title: '', amount: '', categoryId: 1,
    date: new Date().toISOString().split('T')[0],
    type: 'EXPENSE' as 'EXPENSE' | 'INCOME', note: ''
  });


  constructor(
    private expenseService: ExpenseService,
    private auth: AuthService,
    private router: Router,
  ) {}

  async ngOnInit() {
    const [page, cats] = await Promise.all([
      this.expenseService.getExpenses({ type: 'ALL' }),
      this.expenseService.getCategories(),
    ]);
    this.expenses.set(page.content);
    this.categories.set(cats);
    this.isLoading.set(false);
  }

  get filteredExpenses(): Expense[] {
    const type = this.filterType();
    return this.expenses().filter(e => type === 'ALL' || e.type === type);
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(amount);
  }

  openModal()  { this.showModal.set(true); }
  closeModal() { this.showModal.set(false); }

  async saveExpense() {
    const f = this.form();
    const cat = this.categories().find(c => c.id === Number(f.categoryId))!;
    await this.expenseService.addExpense({
      title: f.title, amount: Number(f.amount),
      category: cat, date: f.date, type: f.type, note: f.note,
    });
    const page = await this.expenseService.getExpenses({ type: 'ALL' });
    this.expenses.set(page.content);
    this.closeModal();
    this.form.set({ title: '', amount: '', categoryId: 1, date: new Date().toISOString().split('T')[0], type: 'EXPENSE', note: '' });
  }

  async deleteExpense(id: number) {
    await this.expenseService.deleteExpense(id);
    this.expenses.update(prev => prev.filter(e => e.id !== id));
  }

  navigateTo(path: string) { this.router.navigate([path]); }

  get totalExpense(): number { return this.expenses().filter(e => e.type === 'EXPENSE').reduce((s,e) => s + e.amount, 0); }
  get totalIncome(): number  { return this.expenses().filter(e => e.type === 'INCOME').reduce((s,e) => s + e.amount, 0); }

  updateForm(key: string, value: any) {
    this.form.update(f => ({ ...f, [key]: value }));
  }
}
