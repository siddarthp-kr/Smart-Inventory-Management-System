import { ChangeDetectorRef, Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { LoginButton } from './components/LoginButton';
import { AuthService } from './services/auth';
import { LoginPage } from './components/LoginPage';
import { Api } from './services/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, LoginButton, LoginPage],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('mock-sims-app');
  protected auth = inject(AuthService);
  protected user = this.auth.user;
  protected isLoggedIn = this.auth.isLoggedIn;

  private api = inject(Api);

  alertCount: number = 0;

  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadAlertCount();
  }

  async loadAlertCount() {
    const user = this.auth.user();

    if (!user) {
      this.alertCount = 0;
      return;
    }

    try {
      const response = await this.api.getAlertCount(
        user.storeNumber,
        user.divisionNumber
      );

      this.alertCount = response.alertCount ?? 0;
    } catch (error) {
      console.error('Failed to load alert count:', error);
      this.alertCount = 0; // silent fallback (your decision ✅)
    }

    this.cd.detectChanges();
  }
}
