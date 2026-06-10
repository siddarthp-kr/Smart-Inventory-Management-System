import { Component } from '@angular/core';
import { output} from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component ({
  selector: `login-page`,
  templateUrl: `./template/LoginPageTemplate.html`,
  imports: [
    FormsModule
  ],
  standalone: true
})
export class LoginPage {

  euid = ''
  storeNumber = ''
  divisionNumber = ''

  onLogin(){
    let credentials = {
      euid: this.euid,
      storeNumber: this.storeNumber,
      divisionNumber: this.divisionNumber
    }
  }
}

