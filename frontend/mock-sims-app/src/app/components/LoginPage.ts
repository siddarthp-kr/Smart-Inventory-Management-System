import { Component, inject } from '@angular/core';
import { output} from '@angular/core';
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
  storeNumber = ''
  divisionNumber = ''

  onLogin(){
    let credentials = {
      userEuid: this.euid,
      storeNumber: this.storeNumber,
      divisionNumber: this.divisionNumber
    }
    //DO LOGIN VALIDATION HERE
      if(this.euid !== '' && this.storeNumber !== '' && this.divisionNumber !== '') {
        this.auth.login(credentials)
        this.router.navigate(['/Dashboard'])
      }
  }
}

