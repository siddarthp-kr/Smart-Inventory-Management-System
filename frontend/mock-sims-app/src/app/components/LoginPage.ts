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

  currentUserEuidOutput = output<string>();
  currentUserStoreOutput = output<string>();
  currentUserDivisionOutput = output<string>();

  onLogin(){
    this.currentUserEuidOutput.emit(this.euid);
    this.currentUserStoreOutput.emit(this.storeNumber);
    this.currentUserDivisionOutput.emit(this.divisionNumber);


    this.router.navigate(['/Dashboard']);
  }
}

