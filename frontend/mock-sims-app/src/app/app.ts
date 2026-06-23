import {ChangeDetectorRef, Component, inject, signal} from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import {LoginButton} from './components/LoginButton';
import { AuthService } from './services/auth'

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, LoginButton],
  templateUrl: './app.html',
  //templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('mock-sims-app');
  protected auth = inject(AuthService);
}
