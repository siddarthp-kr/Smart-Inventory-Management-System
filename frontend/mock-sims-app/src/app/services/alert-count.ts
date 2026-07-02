import { Injectable, signal, inject } from '@angular/core';
import { Api } from './api';
import { AuthService } from './auth';

@Injectable({ providedIn: 'root' })
export class AlertCountService {
  private api = inject(Api);
  private auth = inject(AuthService);

  readonly count = signal(0);

  async refresh() {
    const user = this.auth.user();

    if (!user) {
      this.count.set(0);
      return;
    }

    try {
      const response = await this.api.getAlertCount(user.storeNumber, user.divisionNumber);
      this.count.set(response.alertCount ?? 0);
    } catch (error) {
      console.error('Failed to load alert count:', error);
      this.count.set(0);
    }
  }

  decrement(by: number = 1) {
    this.count.update(c => Math.max(0, c - by));
  }
}
