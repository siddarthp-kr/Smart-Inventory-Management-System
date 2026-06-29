// services/auth.service.ts
import { Injectable, signal, computed } from '@angular/core';

export interface User {
  userEuid: string;
  storeNumber: string;
  divisionNumber: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly STORAGE_KEY = 'currentUser';

  // Signal holding the current user (null if not logged in)
  private _user = signal<User | null>(this.loadFromStorage());

  // Public read-only accessors
  readonly user = this._user.asReadonly();
  readonly isLoggedIn = computed(() => this._user() !== null);

  login(user: User): void {
    this._user.set(user);
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
  }

  logout(): void {
    this._user.set(null);
    localStorage.removeItem(this.STORAGE_KEY);
  }

  private loadFromStorage(): User | null {
    const data = localStorage.getItem(this.STORAGE_KEY);
    try {
      return data ? JSON.parse(data) as User : null;
    } catch {
      return null;
    }
  }
}
