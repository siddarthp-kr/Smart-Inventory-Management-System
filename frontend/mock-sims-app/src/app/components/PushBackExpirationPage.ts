import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Api } from '../services/api';

interface PushBackExpirationState {
  alertId: number;
  upcNumber: string;
  productName: string;
  expirationDate: string;
  returnState: {
    alertId: number;
    upcNumber: string;
    productName: string;
    mdAfterDate: string;
    rfiAfterDate: string;
    expirationDate: string;
    departmentNumber: string;
  }
}

@Component({
  templateUrl: './template/PushBackExpirationPageTemplate.html',
  selector: 'push-back-expiration-page',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class PushBackExpirationPage implements OnInit {
  private router = inject(Router);
  private auth = inject(AuthService);
  private api = inject(Api);

  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as PushBackExpirationState | undefined;

    if (!state || state.alertId == null || !state.expirationDate) {
      this.missingState = true;
      return;
    }

    this.alertId = state.alertId;
    this.upcNumber = state.upcNumber;
    this.productName = state.productName;
    this.expirationDate = this.normalizeDateString(state.expirationDate);
    this.minimumNewExpirationDate = this.addDays(this.expirationDate, 1);
    this.returnState = state.returnState
  }

  // Data from router state
  alertId: number = 0;
  upcNumber: string = '';
  productName: string = '';
  expirationDate: string = '';
  minimumNewExpirationDate: string = '';
  returnState: any = {};

  // Page state
  missingState: boolean = false;
  isSubmitting: boolean = false;
  pushBackSuccessful: boolean = false;

  // User-selected new expiration date
  newExpirationDate: string = '';

  // Status message
  statusMessage: string = '';
  statusIsSuccess: boolean = true;

  ngOnInit() {
    if (this.missingState) {
      this.router.navigate(['/AlertsPage']);
      return;
    }
  }

  showMessage(message: string, success: boolean) {
    this.statusMessage = message;
    this.statusIsSuccess = success;

    setTimeout(() => {
      // Keep success message visible after successful push back
      if (!this.pushBackSuccessful) {
        this.statusMessage = '';
      }
    }, 3000);
  }

  /**
   * Ensures dates work with <input type="date">, which expects YYYY-MM-DD.
   */
  normalizeDateString(dateValue: string): string {
    if (!dateValue) {
      return '';
    }

    // Already in YYYY-MM-DD format
    if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
      return dateValue;
    }

    const date = new Date(dateValue);

    if (Number.isNaN(date.getTime())) {
      return '';
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  formatDateForDisplay(dateValue: string): string {
    if (!dateValue) {
      return 'N/A';
    }

    const normalized = this.normalizeDateString(dateValue);

    if (!normalized) {
      return dateValue;
    }

    const [year, month, day] = normalized.split('-').map(Number);

    // Use local date to avoid UTC timezone shifting the displayed date
    const date = new Date(year, month - 1, day);

    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  onDateChange() {
    this.newExpirationDate = this.normalizeDateString(this.newExpirationDate);
  }

  get selectedDateIsBeforeCurrent(): boolean {
    if (!this.newExpirationDate || !this.expirationDate) {
      return false;
    }

    return this.newExpirationDate <= this.expirationDate;
  }

  get canPushBackExpiration(): boolean {
    return (
      !!this.newExpirationDate &&
      !this.selectedDateIsBeforeCurrent &&
      !this.isSubmitting &&
      !this.pushBackSuccessful
    );
  }

  async onPushBackExpirationButtonClick() {
    this.onDateChange();

    if (!this.newExpirationDate) {
      this.showMessage('Select a new expiration date before pushing back the alert.', false);
      return;
    }

    if (this.selectedDateIsBeforeCurrent) {
      this.showMessage('The new expiration date must be after the current expiration date.', false);
      return;
    }

    const user = this.auth.user();

    if (!user) {
      this.showMessage('Failed to push back expiration date. You must be logged in.', false);
      return;
    }

    await this.pushBackExpiration(
      this.alertId,
      this.newExpirationDate,
      user.userEuid
    );
  }

  async pushBackExpiration(
    alertId: number,
    newExpirationDate: string,
    userEuid: string
  ) {
    this.isSubmitting = true;

    let pushBackResponse = {
      responseMessage: 'Could not contact server.',
      responseCode: 503
    };

    try {
      pushBackResponse = await this.api.pushBackItem(
        alertId,
        newExpirationDate,
        userEuid
      );

      if (pushBackResponse?.responseCode === 200) {
        this.pushBackSuccessful = true;

        this.showMessage(
          `Successfully pushed back expiration date to ${this.formatDateForDisplay(newExpirationDate)}.`,
          true
        );
      } else {
        this.showMessage(
          pushBackResponse?.responseMessage || 'Failed to push back expiration date.',
          false
        );
      }
    } catch (error) {
      this.showMessage('Failed to push back expiration date. ' + pushBackResponse.responseMessage, false);
    } finally {
      this.isSubmitting = false;
      this.cd.detectChanges();
    }
  }

  goBackToAlertActions() {
    if (this.returnState) {
      this.router.navigate(['/AlertActionPage'], {
        state: this.returnState
      });
      return;
    }
  }

  goBackToAlerts() {
    this.router.navigate(['/AlertsPage']);
  }
  addDays(dateValue: string, days: number): string {
    if (!dateValue) {
      return '';
    }

    const normalized = this.normalizeDateString(dateValue);

    if (!normalized) {
      return '';
    }

    const [year, month, day] = normalized.split('-').map(Number);

    // Local date prevents timezone shifting issues
    const date = new Date(year, month - 1, day);
    date.setDate(date.getDate() + days);

    const newYear = date.getFullYear();
    const newMonth = String(date.getMonth() + 1).padStart(2, '0');
    const newDay = String(date.getDate()).padStart(2, '0');

    return `${newYear}-${newMonth}-${newDay}`;
  }
}
