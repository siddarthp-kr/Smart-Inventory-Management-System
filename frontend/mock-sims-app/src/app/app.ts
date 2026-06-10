import { Component, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { LoginPage} from './components/LoginPage';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoginPage, RouterLink],
  template: `
    <router-outlet>
    </router-outlet>`,
  //templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('mock-sims-app');
}
