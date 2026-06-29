import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'login-button',
  standalone: true,
  imports: [RouterLink],
  template: `
    @if (auth.isLoggedIn()) {
      <div class="login-info">
        <div class="user-panel">
          <div class="user-avatar">
            {{ auth.user()?.userEuid?.charAt(0) }}
          </div>

          <div class="user-details">
            <div class="user-label">
              {{ auth.user()?.userEuid }}
            </div>
            <div class="user-meta">
              Store {{ auth.user()?.storeNumber }} • Division {{ auth.user()?.divisionNumber }}
            </div>
          </div>
        </div>

        <button class="login-btn" (click)="handleLogout()">Logout</button>
      </div>
    } @else {
      <button class="login-btn" routerLink="/LoginPage">Login</button>
    }
  `,
  styles: [`
    .login-info {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .user-panel {
      display: flex;
      align-items: center;
      gap: 10px;
      background: rgba(255, 255, 255, 0.16);
      border: 1px solid rgba(255, 255, 255, 0.22);
      border-radius: 14px;
      padding: 6px 12px;
      color: white;
    }

    .user-avatar {
      width: 34px;
      height: 34px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.24);
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 14px;
      flex-shrink: 0;
    }

    .user-details {
      display: flex;
      flex-direction: column;
      line-height: 1.2;
    }

    .user-label {
      font-size: 13px;
      font-weight: 700;
      color: white;
      white-space: nowrap;
    }

    .user-meta {
      font-size: 11px;
      color: rgba(255, 255, 255, 0.85);
      white-space: nowrap;
    }

    .login-btn {
      border: none;
      border-radius: 10px;
      padding: 10px 16px;
      font-size: 13px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.18s ease;
      background: white;
      color: #4f46e5;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
    }

    .login-btn:hover {
      transform: translateY(-1px);
      opacity: 0.95;
    }
  `]
})
export class LoginButton {
  protected auth = inject(AuthService);
  private router = inject(Router);

  handleLogout() {
    this.auth.logout();
    this.router.navigate(['']);
  }
}
