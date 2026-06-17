import { Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { AuthService} from '../services/auth';

@Component({
  template: `
    @if(this.isLoggedIn){
      <button class="login-btn" routerLink="/LoginPage">Login</button>
    } @else {
      <button class="login-btn" (click)="handleLogout()">Logout</button>
    }
    `,
  selector: `login-button`,
  imports: [
    RouterLink
  ],
  standalone: true
})
export class LoginButton {
// make it so that this button switches to logout if logged in
  private router = inject(Router)
  private auth = inject(AuthService)
  private user = this.auth.user()
  public isLoggedIn = false
  ngOnInit(){
    this.isLoggedIn = this.user === null
  }

  handleLogout(){
    this.auth.logout()
    this.isLoggedIn = false
    this.router.navigate(['/Dashboard'])
  }

}
