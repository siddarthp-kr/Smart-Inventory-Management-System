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

    const credentials = {
      userEuid: trimmedEuid,
      storeNumber: this.storeNumber,
      divisionNumber: this.divisionNumber
    };

    this.loginError = '';

    if(!this.loginIsValid(credentials).login){
      this.loginError = "Invalid Login Parameters: "
      if(!this.loginIsValid(credentials).userEuid){
        this.loginError += "EUID must be 2 letters followed by 5 digits. "
      }
      if(!this.loginIsValid(credentials).storeNumber){
        this.loginError += "Store Number must be 5 digits. "
      }
      if(!this.loginIsValid(credentials).divisionNumber){
        this.loginError += "Division Number must be 3 digits. "
      }
      console.log(this.loginError)
      return;
    }

    // if (!euidPattern.test(trimmedEuid) && !isTestUser) {
    //   this.loginError = 'EUID must be 2 letters followed by 5 numbers (example: AB12345), or use "test" for testing.';
    //   return;
    // }


    this.auth.login(credentials);
    this.router.navigate(['/Dashboard']);
  }


  loginIsValid(credentials: {userEuid: string, storeNumber: string, divisionNumber: string}){
    let euidIsValid = credentials.userEuid !== null && /^[a-zA-Z]{2}\d{5}$/.test(credentials.userEuid)
    let storeNumberIsValid = credentials.storeNumber && /^\d{5}$/.test(credentials.storeNumber)
    let divisionNumberIsValid = credentials.divisionNumber && /^\d{5}$/.test(credentials.divisionNumber)
    let loginIsValid = euidIsValid && storeNumberIsValid && divisionNumberIsValid

    return {
      login: loginIsValid,
      userEuid: euidIsValid,
      storeNumber: storeNumberIsValid,
      divisionNumber: divisionNumberIsValid
    }

  }
}

