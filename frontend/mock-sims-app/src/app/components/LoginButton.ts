import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'login-button',
  standalone: true,
  imports: [RouterLink],
  template: `
    @if (auth.isLoggedIn()) {
      <button class="login-btn" (click)="handleLogout()">Logout</button>
    } @else {
      <button class="login-btn" routerLink="/LoginPage">Login</button>
    }
  `,
})
export class LoginButton {
  protected auth = inject(AuthService);
  private router = inject(Router);

  handleLogout() {
    this.auth.logout();
    this.router.navigate(['/Dashboard']);
  }
}
