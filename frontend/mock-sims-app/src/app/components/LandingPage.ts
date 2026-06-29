import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth';

@Component({
  selector: 'landing-page',
  templateUrl: './template/LandingPageTemplate.html',
  standalone: true
})
export class LandingPage {
  private router = inject(Router);
  private auth = inject(AuthService);
  //private user = this.auth.user;
  private isLoggedIn = this.auth.isLoggedIn;

  ngOnInit(){
    if(this.isLoggedIn()){
      this.router.navigate(['/OrderPage'])
    }
  }
}
