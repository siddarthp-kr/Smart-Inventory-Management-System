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

  currentUserEuidOutput = output<string>();
  currentUserStoreOutput = output<string>();
  currentUserDivisionOutput = output<string>();

  onLogin(){
    this.currentUserEuidOutput.emit(this.euid);
    this.currentUserStoreOutput.emit(this.storeNumber);
    this.currentUserDivisionOutput.emit(this.divisionNumber);

  }
}

