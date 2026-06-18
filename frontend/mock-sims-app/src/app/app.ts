import {ChangeDetectorRef, Component, signal} from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import {LoginButton} from './components/LoginButton';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, LoginButton],
  template: `
      <div class = "top-bar">
        <h2>Smart Inventory System</h2>
        <login-button />
      </div>

      <nav class = "nav-bar">
        <a routerLink="/Dashboard">Dashboard</a>
        <a routerLink="/OrderPage">Ordering</a>
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
  currentUserEuid = ''
  currentUserStoreNumber = ''
  currentUserDivisionNumber = ''

  assignLoginCredentials(credentials: {euid: string, storeNumber: string, divisionNumber: string}){
    this.currentUserEuid = credentials.euid
    this.currentUserStoreNumber = credentials.storeNumber
    this.currentUserDivisionNumber = credentials.divisionNumber
  }
}
