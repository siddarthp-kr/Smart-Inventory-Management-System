
import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api, MovementInfoRecord, MovementInfoResponse } from '../services/api';
import { AuthService } from '../services/auth';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: '',
  templateUrl: './template/MovementPageTemplate.html',
  imports: [CommonModule, FormsModule, RouterLink],
  standalone: true
})

export class MovementPage{
  private auth = inject(AuthService);
  private api = inject(Api);
  private router = inject(Router);
  constructor(private cd: ChangeDetectorRef) {}

  statusMessage = '';
  statusIsSuccess = true;
  upcSearch: string = '';
  search: string = '';
  allRecords: MovementInfoRecord[] = [];
  filteredRecords: MovementInfoRecord[] = [];

  hasSearched = false;
  isLoading = false;

  pageSizeOptions: number[] = [5,10,50,100,500];
  pageSize: number = 10;
  currentPage: number = 1;

  showMessage(message: string, status: boolean) {
    this.statusMessage = message;
    this.statusIsSuccess = status;

    setTimeout(() => {
      this.statusMessage = '';
      this.cd.detectChanges();
    }, 3000);
  }

  async searchMovements() {
    const user = this.auth.user();

    if (!user) {
      this.showMessage('You must be logged in to view movement history.', false);
      return;
    }

    const upc = this.upcSearch.trim();

    if (upc === '') {
      this.showMessage('Enter a UPC to search for movement history.', false);
      return;
    }

    this.hasSearched = true;
    this.isLoading = true;
    this.search = '';
    this.currentPage = 1;

    let response: MovementInfoResponse;
    try {
      response = await this.api.getMovementInfo(
        user.storeNumber,
        user.divisionNumber,
        upc
      );
    } catch (error) {
      response = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        movements: []
      };
    }

    this.isLoading = false;

    if (response.responseCode !== 200) {
      this.allRecords = [];
      this.filteredRecords = [];

      this.showMessage(
        `Error: ${response.responseCode} ${response.responseMessage}`,
        false
      );

      this.cd.detectChanges();
      return;
    }

    this.allRecords = (response.movements || []).slice();
    this.applySearch();

    if (this.allRecords.length > 0) {
      this.showMessage('Successfully loaded movement history.', true);
    }

    this.cd.detectChanges();
  }

  refresh() {
    if (this.upcSearch.trim() === '') {
      this.showMessage('Enter a UPC before refreshing movement history.', false);
      return;
    }

    this.searchMovements();
  }

  applySearch(){
    const term = this.search.trim().toLowerCase();

    if (term === ''){
      this.filteredRecords = this.allRecords.slice();
    } else{
      this.filteredRecords = this.allRecords.filter(r =>
        r.userEuid?.toLowerCase().includes(term) ||
        r.movementType?.toLowerCase().includes(term)
      );
    }

    this.currentPage = 1;
  }

  getDescription(record: MovementInfoRecord): string {
    if (record.movementType === 'MARKDOWN') {
      const originalPrice = this.formatMoney(record.originalPrice);
      const newPrice = this.formatMoney(record.newPrice);

      return `${record.quantityChanged} units marked down (${originalPrice} → ${newPrice})`;
    }

    if (record.movementType === 'RFI') {
      const reason = this.formatReason(record.reasonCode);

      if (record.sourceBucket) {
        return `${record.quantityChanged} units removed from ${record.sourceBucket} (${reason})`;
      }

      return `${record.quantityChanged} units removed (${reason})`;
    }

    if (record.movementType === 'ORDERED') {
      return `${record.quantityChanged} units ordered`;
    }

    if (record.movementType === 'RECEIVED') {
      if (
        record.qodBeforeTransaction !== null &&
        record.qodBeforeTransaction !== undefined &&
        record.quantityChanged !== null &&
        record.quantityChanged !== undefined
      ) {
        const qodAfterTransaction = record.qodBeforeTransaction + record.quantityChanged;
        return `${record.quantityChanged} units received into QOD (${record.qodBeforeTransaction} → ${qodAfterTransaction})`;
      }

      return `${record.quantityChanged} units received into QOD`;
    }

    return `${record.quantityChanged} units changed`;
  }

  formatReason(reasonCode: string | null): string {
    if (!reasonCode) return 'Unknown';

    if(reasonCode === 'OD'){
      return 'Outdated'
    }

    return reasonCode;
  }

  formatMoney(value: number | null): string {
    if (value === null || value === undefined) {
      return '$0.00';
    }

    return `$${value.toFixed(2)}`;
  }

  formatDateTime(value: string | null): string {
    if (!value) {
      return '';
    }

    const date = new Date(value);

    if (isNaN(date.getTime())) {
      return value;
    }

    const formattedDate = date.toLocaleDateString('en-US', {
      month: '2-digit',
      day: '2-digit',
      year: 'numeric'
    });

    const formattedTime = date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });

    return `${formattedDate} ${formattedTime}`;
  }

  onPageSizeChange() {
    this.currentPage = 1;
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
    }
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredRecords.length / this.pageSize));
  }

  get pagedRecords(): MovementInfoRecord[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return this.filteredRecords.slice(start, start + this.pageSize);
  }

  goBack() {
    this.router.navigate(['/AlertsPage']);
  }
}

