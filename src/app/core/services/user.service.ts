import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { ApiResponse, User } from '../models/expense.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/users`;

  async getProfile(): Promise<User> {
    const response = await firstValueFrom(
      this.http.get<ApiResponse<User>>(`${this.apiUrl}/me`)
    );
    if (!response.success || !response.data) {
      throw new Error(response.message || 'Failed to fetch profile');
    }
    return response.data;
  }
}
