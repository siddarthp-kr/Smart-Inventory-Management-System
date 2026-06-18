import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router} from '@angular/router';
import { AuthService } from '../services/auth';

@Component ({
  selector: `login-page`,
  templateUrl: `./template/LoginPageTemplate.html`,
  imports: [
    FormsModule
  ],
  standalone: true
})
export class LoginPage {
  private auth = inject(AuthService)
  private router = inject(Router)

  euid = ''
  storeNumber = '00045'
  divisionNumber = '014'

  loginError = '';


  onLogin() {
    const trimmedEuid = this.euid.trim();
    const euidPattern = /^[a-zA-Z]{2}\d{5}$/;
    const isTestUser = trimmedEuid.toLowerCase() === 'test';

    if (!euidPattern.test(trimmedEuid) && !isTestUser) {
      this.loginError = 'EUID must be 2 letters followed by 5 numbers (example: AB12345), or use "test" for testing.';
      return;
    }

    this.loginError = '';

    const credentials = {
      userEuid: trimmedEuid,
      storeNumber: this.storeNumber,
      divisionNumber: this.divisionNumber
    };

    this.auth.login(credentials);
    this.router.navigate(['/Dashboard']);
  }


  // onLogin(){
  //   let credentials = {
  //     userEuid: this.euid,
  //     storeNumber: this.storeNumber,
  //     divisionNumber: this.divisionNumber
  //   }
  //   //DO LOGIN VALIDATION HERE
  //     if(this.euid !== '' && this.storeNumber !== '' && this.divisionNumber !== '') {
  //       this.auth.login(credentials)
  //       this.router.navigate(['/Dashboard'])
  //     }
  // }
}

