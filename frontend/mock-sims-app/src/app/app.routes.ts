import { Routes } from '@angular/router';
import {LoginPage} from './components/LoginPage';
import { Dashboard } from './pages/Dashboard';
import { OrderPage } from './components/OrderPage';
import { BohPage } from './components/BohPage';
import { AlertsPage } from './components/AlertsPage';


export const routes: Routes = [
  {
    path: '',
    title: 'SIMS Login',
    component: LoginPage,
  },
  {
    path: 'LoginPage',
    title: 'SIMS Login',
    component: LoginPage,
  },
  {
    path: 'Dashboard',
    title: 'SIMS Dashboard',
    component: Dashboard
  },
  {
    path: 'OrderPage',
    title: "SIMS Ordering",
    component: OrderPage
  },
  {
    path: 'BohPage',
    title: "Balance On Hand",
    component: BohPage
  },
  {
    path: 'AlertsPage',
    title: "Product Expiration Alerts",
    component: AlertsPage
  }
];
