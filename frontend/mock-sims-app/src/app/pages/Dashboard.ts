import { Component, inject } from '@angular/core';
import {AuthService} from '../services/auth';

@Component({
  selector: "app-dashboard",
  standalone: true,
  templateUrl: './template/DashboardTemplate.html'
})
export class Dashboard{
  protected auth = inject(AuthService);
}
