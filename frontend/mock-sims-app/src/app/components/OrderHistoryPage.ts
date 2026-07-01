import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api, OrderHistoryRecord, OrderHistoryResponse } from '../services/api';
import { AuthService } from '../services/auth';
import { Router } from '@angular/router';

interface GroupedOrder {
  orderId: number;
  placedByUserEuid: string;
  orderPlacedTime: string;
  orderReceived: boolean;
  receivedByUserEuid: string | null;
  orderReceivedTime: string | null;
  items: OrderHistoryRecord[];
  expanded: boolean;
}

@Component({
  templateUrl: `./template/OrderHistoryPageTemplate.html`,
  selector: ``,
  imports: [CommonModule, FormsModule],
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
  groupedOrders: GroupedOrder[] = [];
  filteredGroupedOrders: GroupedOrder[] = [];

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
      this.cd.detectChanges();
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

    this.groupOrders();
    this.applySearch()
    this.cd.detectChanges()
  }

  groupOrders(){
    const groupedMap = new Map<number, GroupedOrder>();

    for (const record of this.allRecords){
      const existing = groupedMap.get(record.orderId);

      if (existing){
        existing.items.push(record);
      } else{
        groupedMap.set(record.orderId, {
          orderId:record.orderId,
          placedByUserEuid: record.placedByUserEuid,
          orderPlacedTime: record.orderPlacedTime,
          orderReceived: record.orderReceived,
          receivedByUserEuid: record.receivedByUserEuid,
          orderReceivedTime: record.orderReceivedTime,
          items: [record],
          expanded: false
        });
      }
    }

    this.groupedOrders = Array.from(groupedMap.values()).sort((a, b) => b.orderId - a.orderId);
  }

  // Refresh button handler
  refresh() {
    this.orderHistoryAction()
  }

  // Search bar handler - filters by UPC, product name, user, or order id
  applySearch() {
    const term = this.search.trim().toLowerCase()
    if (term === '') {
      this.filteredGroupedOrders = this.groupedOrders.slice();
    } else {
      this.filteredGroupedOrders = this.groupedOrders.filter(order =>
        order.placedByUserEuid?.toLowerCase().includes(term) ||
        order.receivedByUserEuid?.toLowerCase().includes(term) ||
        this.getStatusText(order).toLowerCase().includes(term) ||
        order.items.some(item =>
          item.productName?.toLowerCase().includes(term) ||
          item.upcNumber?.includes(term)
        )
      );
    }
    // Reset back to first page when filter changes
    this.currentPage = 1
  }

  toggleOrder(orderId: number) {
    const order = this.filteredGroupedOrders.find(o => o.orderId === orderId);
    if (order) {
      order.expanded = !order.expanded;
    }
  }

  async receiveOrder(order: GroupedOrder) {
    const user = this.auth.user();

    if (!user) {
      this.showMessage('Failed to receive order. You must be logged in.', false);
      return;
    }

    if (order.orderReceived) {
      this.showMessage('This order has already been received.', false);
      return;
    }

    try {
      const response = await this.api.receiveOrder(
        user.storeNumber,
        user.divisionNumber,
        user.userEuid,
        order.orderId
      );

      if (response.responseCode === 200) {
        this.showMessage(response.responseMessage, true);

        await this.orderHistoryAction();
      } else {
        this.showMessage(response.responseMessage, false);
      }

    } catch (error: any) {
      console.error('Failed to receive order:', error);

      if (error?.error?.responseCode && error?.error?.responseMessage) {
        this.showMessage(
          `Error: ${error.error.responseCode} ${error.error.responseMessage}`,
          false
        );
      } else {
        this.showMessage('Failed to receive order.', false);
      }
    }

    this.cd.detectChanges();
  }

  getStatusText(order: GroupedOrder): string{
    return order.orderReceived ? 'Received' : 'Pending';
  }

  // For testing purposes
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
    return Math.max(1, Math.ceil(this.filteredGroupedOrders.length / this.pageSize))
  }

  get pagedRecords(): GroupedOrder[] {
    const start = (this.currentPage - 1) * this.pageSize
    return this.filteredGroupedOrders.slice(start, start + this.pageSize)
  }

  // Back navigation
  goBack() {
    this.router.navigate(['/BohPage'])
  }
}
