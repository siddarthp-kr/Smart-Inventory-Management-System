import { Routes } from '@angular/router';
import {LoginPage} from './components/LoginPage';
import { Dashboard } from './pages/Dashboard';
import { BohPage } from './components/BohPage';
import { AlertsPage } from './components/AlertsPage';
import { AddItemPage } from './components/AddItemPage';
import { OrderHistoryPage } from './components/OrderHistoryPage';
import {MovementPage} from './components/MovementPage';
import {LandingPage} from './components/LandingPage';
import {AlertActionPage} from './components/AlertActionPage';
import {MarkdownItemPage} from './components/MarkdownItemPage';
import {RfiItemPage} from './components/RfiItemPage';
import {PushBackExpirationPage} from './components/PushBackExpirationPage';


export const routes: Routes = [
  {
    path: '',
    title: 'SIMS',
    component: LandingPage,
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
  },
  {
    path: 'AlertActionPage',
    title: "Alert Actions",
    component: AlertActionPage
  },
  {
    path: 'MarkdownItemPage',
    title: "Markdown Item",
    component: MarkdownItemPage
  },
  {
    path: 'RfiItemPage',
    title: "Remove Inventory",
    component: RfiItemPage
  },
  {
    path: 'PushBackExpirationPage',
    title: "Push Back Expiration",
    component: PushBackExpirationPage
  }
];
