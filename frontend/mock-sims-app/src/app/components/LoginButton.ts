import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'login-button',
  standalone: true,
  imports: [RouterLink],
  template: `
    @if (auth.isLoggedIn()) {
      <div class = "login-info">
        <span class = "user-label">
          Logged in as: {{auth.user()?.userEuid}}
        </span>
        <button class="login-btn" (click)="handleLogout()">Logout</button>
      </div>
    } @else {
      <button class="login-btn" routerLink="/LoginPage">Login</button>
    }
  `,
  styles: [`
    .login-info{
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .user-label{
      font-size: 14px;
      font-weight: 500;
    }

    .login-btn{
      padding: 8px 12px;
      cursor: pointer;
    }
  `
  ]
})
export class LoginButton {
  protected auth = inject(AuthService);
  private router = inject(Router);

  handleLogout() {
    this.auth.logout();
    this.router.navigate(['/Dashboard']);
  }
}
