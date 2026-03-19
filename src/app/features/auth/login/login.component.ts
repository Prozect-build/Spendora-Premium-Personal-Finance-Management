import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  email       = signal('');
  password    = signal('');
  isLoading   = signal(false);
  errorMsg    = signal('');

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  async onLogin() {
    if (!this.email() || !this.password()) {
      this.errorMsg.set('Please fill in all fields.');
      return;
    }

    this.isLoading.set(true);
    this.errorMsg.set('');

    try {
      const success = await this.auth.login(this.email(), this.password());
      if (success) {
        this.router.navigate(['/dashboard']);
      } else {
        this.errorMsg.set('Invalid email or password.');
      }
    } catch (error) {
      this.errorMsg.set('Login failed. Please try again.');
    } finally {
      this.isLoading.set(false);
    }
  }
}
