import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: '../login/login.component.css', // Reusing login styles for consistency
})
export class RegisterComponent {
  name        = signal('');
  email       = signal('');
  password    = signal('');
  isLoading   = signal(false);
  errorMsg    = signal('');

  constructor(
    private auth: AuthService,
    private router: Router,
  ) {}

  async onRegister() {
    if (!this.name() || !this.email() || !this.password()) {
      this.errorMsg.set('Please fill in all fields.');
      return;
    }

    if (this.password().length < 6) {
      this.errorMsg.set('Password must be at least 6 characters.');
      return;
    }

    this.isLoading.set(true);
    this.errorMsg.set('');

    try {
      const success = await this.auth.register(this.name(), this.email(), this.password());
      if (success) {
        this.router.navigate(['/dashboard']);
      } else {
        this.errorMsg.set('Registration failed. Email might already be in use.');
      }
    } catch (error) {
      this.errorMsg.set('Registration failed. Please try again.');
    } finally {
      this.isLoading.set(false);
    }
  }
}
