import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api, OrderHistoryRecord, OrderHistoryResponse } from '../services/api';
import { AuthService } from '../services/auth';
import { Router, RouterLink } from '@angular/router';


@Component({
  templateUrl: `./template/OrderHistoryPageTemplate.html`,
  selector: ``,
  imports: [CommonModule, FormsModule, RouterLink],
  standalone: true
})
export class OrderHistoryPage implements OnInit {

  private auth = inject(AuthService)
  private api = inject(Api)
  private router = inject(Router)
  constructor(private cd: ChangeDetectorRef) {}

  statusMessage = ''
  statusIsSuccess = true

  // All records fetched from backend
  allRecords: OrderHistoryRecord[] = []

  // Records after applying search filter
  filteredRecords: OrderHistoryRecord[] = []

  // Search input
  search: string = ''

  // Pagination
  pageSizeOptions: number[] = [5, 10, 50, 100, 500]
  pageSize: number = 10
  currentPage: number = 1

  ngOnInit() {
    this.orderHistoryAction()
  }

  showMessage(message: string, status: boolean) {
    this.statusMessage = message;
    this.statusIsSuccess = status;

    setTimeout(() => {
      this.statusMessage = '';
    }, 3000);
  }

  async orderHistoryAction() {
    const user = this.auth.user()

    if (!user) {
      this.showMessage('Failed to open order history. You must be logged in to see order history.', false)
      return
    }

    let orderHistoryResponse: OrderHistoryResponse

    try {
      orderHistoryResponse = await this.api.getOrderHistory(user.storeNumber, user.divisionNumber)
      console.log(orderHistoryResponse)
    } catch (error) {
      orderHistoryResponse = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        orders: []
      }
    }

    if(orderHistoryResponse.responseCode !== 200){
      this.showMessage(`Error: ${orderHistoryResponse.responseCode} ${orderHistoryResponse.responseMessage}`, false)
    }

    // Sort by productOrderId descending (just in case backend ordering changes)
    this.allRecords = (orderHistoryResponse.orders || [])
      .slice()
      .sort((a, b) => b.orderId - a.orderId)

    this.applySearch()
    this.cd.detectChanges()
  }

  // Refresh button handler
  refresh() {
    this.orderHistoryAction()
  }

  // Search bar handler - filters by UPC or product name
  applySearch() {
    const term = this.search.trim().toLowerCase()
    if (term === '') {
      this.filteredRecords = this.allRecords.slice()
    } else {
      this.filteredRecords = this.allRecords.filter(r =>
        (r.productName?.toLowerCase().includes(term)) ||
        (r.upcNumber?.includes(term))
      )
    }
    // Reset back to first page when filter changes
    this.currentPage = 1
  }

  // Pagination handlers
  onPageSizeChange() {
    this.currentPage = 1
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++
    }
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--
    }
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredRecords.length / this.pageSize))
  }

  get pagedRecords(): OrderHistoryRecord[] {
    const start = (this.currentPage - 1) * this.pageSize
    return this.filteredRecords.slice(start, start + this.pageSize)
  }

  // Back navigation
  goBack() {
    this.router.navigate(['/BohPage'])
  }
}
