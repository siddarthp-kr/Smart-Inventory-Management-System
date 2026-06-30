import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import {Api} from '../services/api';

interface RfiItemState {
  alertId: number;
  upcNumber: string;
  productName: string;
  returnState: {
    alertId: number;
    upcNumber: string;
    productName: string;
    mdBeforeDate: string;
    rfiBeforeDate: string;
    expirationDate: string;
    departmentNumber: string;
  }
}

@Component({
  templateUrl: './template/RfiItemPageTemplate.html',
  selector: 'rfi-item-page',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class RfiItemPage implements OnInit {
  private router = inject(Router);
  private auth = inject(AuthService);
  private api = inject(Api);

  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as RfiItemState | undefined;

    if (!state || state.alertId == null || !state.upcNumber) {
      this.missingState = true;
      return;
    }

    this.alertId = state.alertId;
    this.upcNumber = state.upcNumber;
    this.productName = state.productName;
    this.returnState = state.returnState;
  }

  // Data from router state
  alertId: number = 0;
  upcNumber: string = '';
  productName: string = '';

  returnState: any = null;

  // Page state
  missingState: boolean = false;
  isSubmitting: boolean = false;
  rfiSuccessful: boolean = false;

  // User-selected quantity
  quantity: number = 0;

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
      // Keep success message visible after successful RFI
      if (!this.rfiSuccessful) {
        this.statusMessage = '';
      }
    }, 3000);
  }

  decrementQuantity() {
    if (this.rfiSuccessful || this.isSubmitting) {
      return;
    }

    if (this.quantity > 0) {
      this.quantity--;
    }
  }

  incrementQuantity() {
    if (this.rfiSuccessful || this.isSubmitting) {
      return;
    }

    this.quantity++;
  }

  sanitizeQuantity() {
    if (this.quantity == null || Number.isNaN(Number(this.quantity))) {
      this.quantity = 0;
      return;
    }

    this.quantity = Math.floor(Number(this.quantity));

    if (this.quantity < 0) {
      this.quantity = 0;
    }
  }

  async onRfiButtonClick() {
    this.sanitizeQuantity();

    if (this.quantity <= 0) {
      this.showMessage('Select a quantity greater than 0 before removing the item from inventory.', false);
      return;
    }

    const user = this.auth.user();

    if (!user) {
      this.showMessage('Failed to remove item from inventory. You must be logged in.', false);
      return;
    }

    await this.removeFromInventory(
      this.alertId,
      this.upcNumber,
      this.quantity,
      user.userEuid,
      user.storeNumber,
      user.divisionNumber
    );
  }

  async removeFromInventory(
    alertId: number,
    upcNumber: string,
    quantity: number,
    userEuid: string,
    storeNumber: string,
    divisionNumber: string
  ) {
    this.isSubmitting = true;

    let rfiResponse = {
      responseMessage: 'Could not contact server.',
      responseCode: 503
    };

    try {
      rfiResponse = await this.api.rfiItem(
        alertId,
        upcNumber,
        quantity,
        userEuid,
        storeNumber,
        divisionNumber
      );

      if (rfiResponse?.responseCode === 200) {
        this.rfiSuccessful = true;
        this.showMessage(
          `Successfully removed ${quantity} item(s) for UPC ${upcNumber} from inventory.`,
          true
        );
      } else {
        this.showMessage(
          rfiResponse?.responseMessage || 'Failed to remove item from inventory.',
          false
        );
      }
    } catch (error) {
      this.showMessage('Failed to remove item from inventory. ' + rfiResponse.responseMessage, false);
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
}
