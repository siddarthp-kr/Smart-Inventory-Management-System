import { Component, inject } from '@angular/core';
import {AuthService} from '../services/auth';
import { ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Api } from '../services/api';

@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [RouterModule],
  templateUrl: './template/DashboardTemplate.html'
})
export class Dashboard{
  protected auth = inject(AuthService);
  private api = inject(Api);

  alertCount: number = 0;
  alertCountLoadFailed: boolean = false;

  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadAlertCount();
  }

  async loadAlertCount(){
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
      this.alertCountLoadFailed = false;
    } catch (error) {
      console.error('Failed to load alert count:', error);
      this.alertCount = 0;
      this.alertCountLoadFailed = true;
    }

    this.cd.detectChanges();
  }
}
