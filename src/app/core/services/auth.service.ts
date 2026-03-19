import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { User, ApiResponse } from '../models/expense.model';
import { environment } from '../../../environments/environment';
import { UserService } from './user.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private userService = inject(UserService);
  private apiUrl = `${environment.apiUrl}/auth`;

  private _token = signal<string | null>(null);
  private _user  = signal<User | null>(null);

  readonly isAuthenticated = computed(() => this._token() !== null);
  readonly currentUser     = computed(() => this._user());
  readonly token           = computed(() => this._token());

  async login(email: string, password: string): Promise<boolean> {
    try {
      const response = await firstValueFrom(
        this.http.post<ApiResponse<{ token: string }>>(`${this.apiUrl}/login`, { email, password })
      );
      
      if (response.success && response.data) {
        this._token.set(response.data.token);
        const userProfile = await this.userService.getProfile();
        this._user.set(userProfile);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Login failed:', error);
      return false;
    }
  }

  async register(name: string, email: string, password: string): Promise<boolean> {
    try {
      const response = await firstValueFrom(
        this.http.post<ApiResponse<{ token: string }>>(`${this.apiUrl}/register`, { name, email, password })
      );
      
      if (response.success && response.data) {
        this._token.set(response.data.token);
        const userProfile = await this.userService.getProfile();
        this._user.set(userProfile);
        return true;
      }
      return false;
    } catch (error) {
      console.error('Registration failed:', error);
      return false;
    }
  }

  logout(): void {
    this._token.set(null);
    this._user.set(null);
  }
}
