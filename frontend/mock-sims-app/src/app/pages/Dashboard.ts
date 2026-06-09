import { Component } from '@angular/core';

import { AlertsPage } from '../components/AlertsPage';
import { BohPage } from '../components/BohPage';
import { OrderPage } from '../components/OrderPage';


@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [AlertsPage, BohPage, OrderPage],
  templateUrl: './template/DashboardTemplate.html'
})
export class Dashboard{}
