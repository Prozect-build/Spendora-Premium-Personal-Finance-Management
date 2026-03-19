import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  auth = inject(AuthService);
  user = this.auth.currentUser;

  isEditing = signal(false);
  name = signal('');
  email = signal('');

  constructor() {
    const currentUser = this.user();
    if (currentUser) {
      this.name.set(currentUser.name);
      this.email.set(currentUser.email);
    }
  }

  toggleEdit() {
    this.isEditing.set(!this.isEditing());
  }

  saveProfile() {
    // In a real app, you'd call a service to update the profile
    console.log('Saving profile:', { name: this.name(), email: this.email() });
    this.isEditing.set(false);
  }

  logout() {
    this.auth.logout();
    window.location.href = '/login';
  }
}
