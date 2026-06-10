import { Routes } from '@angular/router';
import {LoginPage} from './components/LoginPage';
import { Dashboard } from './pages/Dashboard';
import { OrderPage } from './components/OrderPage';
import { BohPage } from './components/BohPage';
import { AlertsPage } from './components/AlertsPage';


export const routes: Routes = [
  {
    path: 'LoginPage',
    title: 'login page',
    component: LoginPage,
  },
  { path: '', component: Dashboard},
  { path: 'ordering', component: OrderPage},
  { path: 'BohPage', component: BohPage},
  { path: 'AlertsPage', component: AlertsPage}
];
