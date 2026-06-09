import { Routes } from '@angular/router';
import {LoginPage} from './components/LoginPage';
import { Dashboard } from './pages/Dashboard';

export const routes: Routes = [
  {
    path: 'LoginPage',
    title: 'login page',
    component: LoginPage,
  },
  { path: '', component: Dashboard}
];
