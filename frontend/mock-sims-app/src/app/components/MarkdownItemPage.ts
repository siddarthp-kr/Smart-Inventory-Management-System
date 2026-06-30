import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { Api } from '../services/api';

interface MarkdownItemState {
  alertId: number;
  upcNumber: string;
  productName: string;

  mdBeforeDate: string;
  rfiBeforeDate: string;
  expirationDate: string;

  returnState: {
    alertId: number;
    upcNumber: string;
    productName: string;
    mdBeforeDate: string;
    rfiBeforeDate: string;
    expirationDate: string;
  }
}

@Component({
  templateUrl: './template/MarkdownItemPageTemplate.html',
  selector: 'markdown-item-page',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class MarkdownItemPage implements OnInit {
  private router = inject(Router);
  private auth = inject(AuthService);
  private api = inject(Api);

  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as MarkdownItemState | undefined;

    if (!state || state.alertId == null || !state.upcNumber) {
      this.missingState = true;
      return;
    }

    this.alertId = state.alertId;
    this.upcNumber = state.upcNumber;
    this.productName = state.productName;
    this.returnState = state.returnState;
    this.returnState = {
      alertId: this.alertId,
      upcNumber: this.upcNumber,
      productName: this.productName,
      mdBeforeDate: state.mdBeforeDate,
      rfiBeforeDate: state.rfiBeforeDate,
      expirationDate: state.expirationDate
    }

  }

  // Data from router state
  alertId: number = 0;
  upcNumber: string = '';
  productName: string = '';

  returnState: any = null;

  // Page state
  missingState: boolean = false;
  isLoading: boolean = false;
  isSubmitting: boolean = false;
  markdownSuccessful: boolean = false;

  // Price information
  originalPrice: number = 0;
  newPrice: number = 0;

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

    this.getMarkdownInfo(this.upcNumber, this.alertId);
  }

  showMessage(message: string, success: boolean) {
    this.statusMessage = message;
    this.statusIsSuccess = success;

    setTimeout(() => {
      // Keep success message visible after successful markdown
      if (!this.markdownSuccessful) {
        this.statusMessage = '';
      }
    }, 3000);
  }

  async getMarkdownInfo(upcNumber: string, alertId: number) {
    this.isLoading = true;

    try {
      const markdownInfo = await this.api.getMarkdownInfo(upcNumber, alertId);

      this.originalPrice = markdownInfo.originalPrice;
      this.newPrice = markdownInfo.newPrice;
    } catch (error) {
      this.showMessage('Failed to load markdown information. Error 503: Failed to contact server.', false);
    } finally {
      this.isLoading = false;
      this.cd.detectChanges();
    }
  }

  decrementQuantity() {
    if (this.markdownSuccessful || this.isSubmitting) {
      return;
    }

    if (this.quantity > 0) {
      this.quantity--;
    }
  }

  incrementQuantity() {
    if (this.markdownSuccessful || this.isSubmitting) {
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

  async onMarkdownButtonClick() {
    this.sanitizeQuantity();

    if (this.quantity <= 0) {
      this.showMessage('Select a quantity greater than 0 before marking down the item.', false);
      return;
    }

    const user = this.auth.user();

    if (!user) {
      this.showMessage('Failed to markdown item. You must be logged in.', false);
      return;
    }

    await this.markdownItem(
      this.alertId,
      this.upcNumber,
      this.quantity,
      user.userEuid,
      user.storeNumber,
      user.divisionNumber
    );
  }

  async markdownItem(
    alertId: number,
    upcNumber: string,
    quantity: number,
    userEuid: string,
    storeNumber: string,
    divisionNumber: string
  ) {
    this.isSubmitting = true;
    let markdownResponse = {
      responseMessage: "Could not contact server.",
      responseCode: 503
    };

    try {
      markdownResponse = await this.api.markdownItem(
        alertId,
        upcNumber,
        quantity,
        userEuid,
        storeNumber,
        divisionNumber
      );

      if (markdownResponse?.responseCode === 200) {
        this.markdownSuccessful = true;
        this.showMessage(
          `Successfully marked down ${quantity} item(s) for UPC ${upcNumber}.`,
          true
        );
      } else {
        this.showMessage(
          markdownResponse?.responseMessage || 'Failed to markdown item.',
          false
        );
      }
    } catch (error) {
      this.showMessage('Failed to markdown item. ' + markdownResponse.responseMessage, false);
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
