import { Component, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { LoginPage} from './components/LoginPage';
import {Dashboard} from './pages/Dashboard';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLink,],
  template: `
    <div class = "top-bar">
      <h2>Smart Inventory System</h2>
      <button class = "login-btn" routerLink="/LoginPage">Login</button>
    </div>

    <nav class = "nav-bar">
      <a routerLink="/">Dashboard</a>
      <a routerLink="/ordering">Ordering</a>
      <a routerLink="/BohPage">BOH</a>
      <a routerLink="/AlertsPage">Alerts</a>
    </nav>

    <div class = "content">
      <router-outlet></router-outlet>
    </div>
  `,
  //templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('mock-sims-app');
}
