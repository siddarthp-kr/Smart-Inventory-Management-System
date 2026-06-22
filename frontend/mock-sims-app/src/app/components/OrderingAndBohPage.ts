import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import {
  Api,
  BohRecord,
  DepartmentInfoRecord,
  PlaceOrderResponse,
  ProductItem,
  ProductsResponse
} from '../services/api';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-ordering-and-boh-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './template/OrderingAndBohPageTemplate.html'
})
export class OrderingAndBohPage implements OnInit {
  private api = inject(Api)
  private auth = inject(AuthService)
  constructor(private cd: ChangeDetectorRef) {}

  // === Shared filter state ===
  search: string = ''
  readonly ALL_DEPARTMENTS = ''
  selectedDepartment: string = this.ALL_DEPARTMENTS
  departmentInfo: DepartmentInfoRecord[] = []

  // === Ordering state ===
  products: ProductItem[] = []
  orderingResults: ProductItem[] = []
  orderMessage: string = ''
  orderIsSuccess: boolean = false

  // === BOH state ===
  allBohRecords: BohRecord[] = []
  filteredBohRecords: BohRecord[] = []
  bohStatusMessage: string = ''
  bohStatusIsSuccess: boolean = true

  // BOH pagination
  pageSizeOptions: number[] = [5, 10, 50, 100, 500]
  pageSize: number = 10
  currentPage: number = 1

  async ngOnInit() {
    await this.loadProducts()
    await this.getDepartmentInfo()
    await this.getBohInfo()
    this.applyFilters()
  }

  // ============================================================
  // Loading
  // ============================================================

  async loadProducts() {
    const user = this.auth.user()

    if (!user) {
      this.showOrderMessage('You must be logged in to load products.', false)
      return
    }

    try {
      const productResponse: ProductsResponse = await this.api.getProducts(
        user.storeNumber,
        user.divisionNumber
      )
      this.products = productResponse.products
    } catch (error) {
      console.error('Failed to load products:', error)
      this.showOrderMessage('Failed to load products from server.', false)
    }
  }

  async getDepartmentInfo() {
    try {
      const response = await this.api.getDepartmentInfo()
      this.departmentInfo = response.departmentInfoRecords
    } catch (error) {
      this.showBohMessage('Failed to load department info. Error 503: Failed to contact server', false)
    }
  }

  async getBohInfo() {
    const user = this.auth.user()

    if (!user) {
      this.showBohMessage('Failed to load balance on hand info. You must be logged in.', false)
      return
    }

    try {
      const response = await this.api.getBohInfo(user.storeNumber, user.divisionNumber)
      this.allBohRecords = response.products
    } catch (error) {
      this.allBohRecords = []
      this.showBohMessage('Failed to load balance on hand info. Error 503: Failed to contact server', false)
    }

    this.applyFilters()
    this.cd.detectChanges()
  }

  // Refresh button reloads everything that depends on BOH data
  refresh() {
    this.getBohInfo()
  }

  // ============================================================
  // Shared filter handlers
  // ============================================================

  onDepartmentChange() {
    // Clear search so it doesn't carry over to a new department
    this.search = ''
    this.applyFilters()
  }

  applyFilters() {
    const term = this.search.trim().toLowerCase()

    // ----- BOH filtering -----
    let bohRecords = this.allBohRecords.slice()

    if (this.selectedDepartment !== this.ALL_DEPARTMENTS) {
      bohRecords = bohRecords.filter(r => r.departmentNumber === this.selectedDepartment)
    }

    if (term !== '') {
      bohRecords = bohRecords.filter(r =>
        (r.productName?.toLowerCase().includes(term)) ||
        (r.upcNumber?.includes(term))
      )
    }

    this.filteredBohRecords = bohRecords
    this.currentPage = 1

    // ----- Ordering filtering -----
    // We use the BOH records to determine which products belong to which department,
    // since the ProductItem interface itself doesn't carry a department.
    let productsList = this.products.slice()

    if (this.selectedDepartment !== this.ALL_DEPARTMENTS) {
      const upcsInDept = new Set(
        this.allBohRecords
          .filter(b => b.departmentNumber === this.selectedDepartment)
          .map(b => b.upcNumber)
      )
      productsList = productsList.filter(p => upcsInDept.has(p.upcNumber))
    }

    if (term !== '') {
      productsList = productsList.filter(p =>
        p.productName.toLowerCase().includes(term) ||
        p.upcNumber.includes(term)
      )
    }

    this.orderingResults = productsList
  }

  // ============================================================
  // Ordering actions
  // ============================================================

  showOrderMessage(text: string, success: boolean) {
    this.orderMessage = text
    this.orderIsSuccess = success
    setTimeout(() => {
      this.orderMessage = ''
    }, 3000)
  }

  async orderAction(product: ProductItem, quantity: number, messageInput: any) {
    const user = this.auth.user()

    if (!user) {
      this.showOrderMessage('Failed to place order. You must be logged in to place an order', false)
      return
    }
    if (!quantity || quantity <= 0) {
      this.showOrderMessage('Enter a valid quantity', false)
      return
    }

    let orderResponse: PlaceOrderResponse
    try {
      orderResponse = await this.api.placeOrder(
        user.storeNumber,
        user.divisionNumber,
        user.userEuid,
        product.upcNumber,
        quantity
      )
    } catch (error) {
      orderResponse = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        orderId: -1
      }
    }

    const successOrder = orderResponse.responseCode === 200

    if (successOrder) {
      this.showOrderMessage(
        `Order placed successfully! Product: ${product.productName} | Quantity: ${quantity} | Order ID: ${orderResponse.orderId}`,
        true
      )
      // After a successful order, refresh BOH so qty changes show up immediately
      await this.getBohInfo()
    } else {
      this.showOrderMessage(
        `Failed to place order for ${product.productName}. Error: ${orderResponse.responseCode} ${orderResponse.responseMessage}`,
        false
      )
    }

    this.cd.detectChanges()
    messageInput.value = ''
  }

  // ============================================================
  // BOH status message + pagination
  // ============================================================

  showBohMessage(text: string, success: boolean) {
    this.bohStatusMessage = text
    this.bohStatusIsSuccess = success
    setTimeout(() => {
      this.bohStatusMessage = ''
    }, 3000)
  }

  onPageSizeChange() {
    this.currentPage = 1
  }

  nextPage() {
    if (this.currentPage < this.totalPages) this.currentPage++
  }

  previousPage() {
    if (this.currentPage > 1) this.currentPage--
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.filteredBohRecords.length / this.pageSize))
  }

  get pagedBohRecords(): BohRecord[] {
    const start = (this.currentPage - 1) * this.pageSize
    return this.filteredBohRecords.slice(start, start + this.pageSize)
  }

  get isSearching(): boolean {
    return this.search.trim() !== ''
  }
}
