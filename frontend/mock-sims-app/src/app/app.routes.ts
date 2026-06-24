import { Routes } from '@angular/router';
import {LoginPage} from './components/LoginPage';
import { Dashboard } from './pages/Dashboard';
import { OrderPage } from './components/OrderPage';
import { BohPage } from './components/BohPage';
import { AlertsPage } from './components/AlertsPage';
import { AddItemPage } from './components/AddItemPage';
import { OrderHistoryPage } from './components/OrderHistoryPage';
import {MovementPage} from './components/MovementPage';


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
    path: 'AddItemPage',
    title: "SIMS Add Item",
    component: AddItemPage
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
  },
  {
    path: 'OrderHistoryPage',
    title: "Order History",
    component: OrderHistoryPage
  },
  {
    path: 'MovementPage',
    title: "Movement",
    component: MovementPage
  }
];
