import { Component, inject } from '@angular/core';
import {AuthService} from '../services/auth';
import { ChangeDetectorRef } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [RouterModule],
  templateUrl: './template/DashboardTemplate.html'
})
export class Dashboard{
  protected auth = inject(AuthService);
  constructor(private cd: ChangeDetectorRef) {}
}
