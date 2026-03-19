import { Component, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  userName = computed(() => this.auth.currentUser()?.name || 'User');
  initial = computed(() => this.userName().charAt(0).toUpperCase());

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
