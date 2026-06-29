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
  mdAfterDate: string;
  rfiAfterDate: string;
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
  private api = inject(Api)
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
    this.mdAfterDate = state.mdAfterDate
    this.rfiAfterDate = state.rfiAfterDate
    this.expirationDate = state.expirationDate
  }

  // Alert info carried over from PDM Alerts page
  alertId: number = -1
  upcNumber: string = ''
  productName: string = ''
  mdAfterDate: string = ''
  rfiAfterDate: string = ''
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

  // Whether each action condition is met
  get canMarkdown(): boolean {
    // Condition: expiration date is on or before mdAfterDate
    return this.expirationDate <= this.mdAfterDate
  }

  get canRemoveFromInventory(): boolean {
    // Condition: expiration date is on or before rfiAfterDate
    return this.expirationDate <= this.rfiAfterDate
  }

  get canPushBackExpiration(): boolean {
    // Third action is always available — user decides if exp date is inaccurate
    return true
  }

  formatDate(date: Date): string {
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  async markdownItem() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    try {
      // TODO: call markdown API method
      // e.g. await this.api.markdownItem(user.storeNumber, user.divisionNumber, this.alertId, this.upcNumber)
      this.showMessage('Item marked down successfully.', true)
    } catch (error) {
      this.showMessage('Failed to mark down item. Error 503: Failed to contact server.', false)
    }

    this.cd.detectChanges()
  }

  async removeFromInventory() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    try {
      // TODO: call remove from inventory API method
      // e.g. await this.api.removeFromInventory(user.storeNumber, user.divisionNumber, this.alertId, this.upcNumber)
      this.showMessage('Item removed from inventory successfully.', true)
    } catch (error) {
      this.showMessage('Failed to remove item from inventory. Error 503: Failed to contact server.', false)
    }

    this.cd.detectChanges()
  }

  async pushBackExpirationDate() {
    const user = this.auth.user()
    if (!user) {
      this.showMessage('You must be logged in to take action.', false)
      return
    }

    try {
      // TODO: call push back expiration date API method
      // e.g. await this.api.pushBackExpiration(user.storeNumber, user.divisionNumber, this.alertId, this.upcNumber)
      this.showMessage('Expiration date pushed back successfully.', true)
    } catch (error) {
      this.showMessage('Failed to push back expiration date. Error 503: Failed to contact server.', false)
    }

    this.cd.detectChanges()
  }


}
