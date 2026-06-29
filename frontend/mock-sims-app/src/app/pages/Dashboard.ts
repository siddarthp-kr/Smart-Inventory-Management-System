import { Component, inject } from '@angular/core';
import {AuthService} from '../services/auth';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: "app-dashboard",
  standalone: true,
  templateUrl: './template/DashboardTemplate.html'
})
export class Dashboard{
  protected auth = inject(AuthService);
  constructor(private cd: ChangeDetectorRef) {}
}
