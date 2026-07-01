import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { LoginButton } from './components/LoginButton';
import { AuthService } from './services/auth';
import { LoginPage } from './components/LoginPage';
import { Api } from './services/api';
import { AlertCountService } from './services/alert-count';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, LoginButton, LoginPage],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected auth = inject(AuthService);
  protected user = this.auth.user;
  protected isLoggedIn = this.auth.isLoggedIn;

  private api = inject(Api);
  protected alertCountService = inject(AlertCountService);

  // Signal from the service — call as alertCount() in the template
  alertCount = this.alertCountService.count;

  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit() {
    this.alertCountService.refresh();
  }
}
