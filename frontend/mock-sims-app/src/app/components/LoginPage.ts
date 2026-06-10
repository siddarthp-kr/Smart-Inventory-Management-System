import { Component, inject } from '@angular/core';
import { output} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router} from '@angular/router';

@Component ({
  selector: `login-page`,
  templateUrl: `./template/LoginPageTemplate.html`,
  imports: [
    FormsModule
  ],
  standalone: true
})
export class LoginPage {
  private router = inject(Router);

  euid = ''
  storeNumber = ''
  divisionNumber = ''

  onLogin(){
    let credentials = {
      euid: this.euid,
      storeNumber: this.storeNumber,
      divisionNumber: this.divisionNumber
    }
      if(this.euid !== '' && this.storeNumber !== '' && this.divisionNumber !== '') {
        this.router.navigate(['/Dashboard'])
      }
  }
}

