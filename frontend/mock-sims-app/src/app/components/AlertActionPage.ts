import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Api } from '../services/api';

interface AlertActionState {
  alertId: number;
  upcNumber: string;
  productName: string;
  mdBeforeDate: string;
  rfiBeforeDate: string;
  expirationDate: string;
}

@Component({
  selector: 'alert-action-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './template/AlertActionPageTemplate.html'
})
export class AlertActionPage implements OnInit {
  private router = inject(Router)
  private auth = inject(AuthService)
  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation()
    const state = nav?.extras?.state as AlertActionState | undefined

    if (!state || state.alertId == null) {
      // User landed here without valid state — go back
      this.missingState = true
      return
    }

    this.alertId = state.alertId
    this.upcNumber = state.upcNumber
    this.productName = state.productName
    this.mdBeforeDate = state.mdBeforeDate
    this.rfiBeforeDate = state.rfiBeforeDate
    this.expirationDate = state.expirationDate

    console.log(this.expirationDate);
  }

  // Alert info carried over from PDM Alerts page
  alertId: number = -1
  upcNumber: string = ''
  productName: string = ''
  mdBeforeDate: string = ''
  rfiBeforeDate: string = ''
  expirationDate: string = ''

  // Status message
  statusMessage: string = ''
  statusIsSuccess: boolean = true

  // True if state was missing (e.g. user navigated directly to URL)
  missingState: boolean = false

  ngOnInit() {
    if (this.missingState) {
      this.router.navigate(['/AlertsPage'])
    }
  }

  showMessage(text: string, success: boolean) {
    this.statusMessage = text
    this.statusIsSuccess = success

    setTimeout(() => {
      this.statusMessage = ''
    }, 3000)
  }

  goBack() {
    this.router.navigate(['/AlertsPage'])
  }


  markdownItem() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    this.router.navigate(['/MarkdownItemPage'], {
      state: {
        alertId: this.alertId,
        upcNumber: this.upcNumber,
        productName: this.productName,
        returnState: {
          alertId: this.alertId,
          upcNumber: this.upcNumber,
          productName: this.productName,
          mdBeforeDate: this.mdBeforeDate,
          rfiBeforeDate: this.rfiBeforeDate,
          expirationDate: this.expirationDate
        }
      }
    })

    this.cd.detectChanges()
  }

  removeFromInventory() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    this.router.navigate(['/RfiItemPage'], {
      state: {
        alertId: this.alertId,
        upcNumber: this.upcNumber,
        productName: this.productName,
        returnState: {
          alertId: this.alertId,
          upcNumber: this.upcNumber,
          productName: this.productName,
          mdBeforeDate: this.mdBeforeDate,
          rfiBeforeDate: this.rfiBeforeDate,
          expirationDate: this.expirationDate
        }
      }
    })

    this.cd.detectChanges()
  }

  pushBackExpirationDate() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    this.router.navigate(['/PushBackExpirationPage'], {
      state: {
        alertId: this.alertId,
        expirationDate: this.expirationDate,
        returnState: {
          alertId: this.alertId,
          upcNumber: this.upcNumber,
          productName: this.productName,
          mdBeforeDate: this.mdBeforeDate,
          rfiBeforeDate: this.rfiBeforeDate,
          expirationDate: this.expirationDate
        }
      }
    })

    this.cd.detectChanges()
  }


}
