
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

    if (response.responseCode !== 200) {
      this.showMessage(
        `Error: ${response.responseCode} ${response.responseMessage}`,
        false
      );
    }

    this.allRecords = (response.movements || []).slice();
    this.applySearch();
    this.cd.detectChanges();
  }

  refresh(){
    this.searchMovements();
  }

  applySearch(){
    const term = this.search.trim().toLowerCase();

    if (term === ''){
      this.filteredRecords = this.allRecords.slice();
    } else{
      this.filteredRecords = this.allRecords.filter(r =>
        r.productName?.toLowerCase().includes(term) ||
        r.userEuid?.toLowerCase().includes(term) ||
        r.movementType?.toLowerCase().includes(term)
      );
    }

    this.currentPage = 1;
  }


  getDescription(record: MovementInfoRecord): string {
    if (record.movementType === 'MARKDOWN') {
      return `${record.quantityChanged} units marked down ($${record.originalPrice} → $${record.newPrice})`;
    }

    if (record.movementType === 'RFI') {
      const reason = this.formatReason(record.reasonCode);
      return `${record.quantityChanged} units removed (${reason})`;
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

