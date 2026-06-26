import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api, BohRecord, DepartmentInfoRecord } from '../services/api';
import { AuthService } from '../services/auth';
import {RouterLink} from '@angular/router';

interface CartItem{
  upcNumber: string;
  productName: string;
  quantity: number;
}

@Component({
  selector: 'app-boh-page',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './template/BohPageTemplate.html'
})
export class BohPage implements OnInit {
  private api = inject(Api)
  private auth = inject(AuthService)
  constructor(private cd: ChangeDetectorRef) {}

  // Department dropdown data
  departmentInfo: DepartmentInfoRecord[] = []

  // All BOH rows fetched from the backend
  allBohRecords: BohRecord[] = []

  // Records after department + search filtering
  filteredRecords: BohRecord[] = []

  // Sentinel value indicating "All Departments"
  readonly ALL_DEPARTMENTS = ''
  selectedDepartment: string = this.ALL_DEPARTMENTS

  // Search input
  search: string = ''

  // Pagination
  pageSizeOptions: (number | 'ALL')[] = ['ALL',5,10,50,100,500];
  pageSize: number | 'ALL' = 10;
  currentPage: number = 1

  // Status message (same pattern as other pages)
  statusMessage: string = ''
  statusIsSuccess: boolean = true

  cartItems: CartItem[] = [];
  pendingQuantities: Record<string, number> = {};

  ngOnInit() {
    this.getDepartmentInfo()
    this.getBohInfo()
  }

  showMessage(text: string, success: boolean) {
    this.statusMessage = text;
    this.statusIsSuccess = success;

    setTimeout(() => {
      this.statusMessage = '';
    }, 3000);
  }

  async getDepartmentInfo() {
    try {
      const response = await this.api.getDepartmentInfo()
      this.departmentInfo = response.departmentInfoRecords
      console.log(this.departmentInfo)
    } catch (error) {
      this.showMessage('Failed to load department info. Error 503: Failed to contact server', false)
    }
  }

  async getBohInfo() {
    const user = this.auth.user()

    if (!user) {
      this.showMessage('Failed to load balance on hand info. You must be logged in.', false)
      return
    }

    try {
      const response = await this.api.getBohInfo(user.storeNumber, user.divisionNumber)
      console.log(response)
      this.allBohRecords = response.products
    } catch (error) {
      this.allBohRecords = []
      this.showMessage('Failed to load balance on hand info. Error 503: Failed to contact server', false)
    }

    this.applyFilters()
    this.cd.detectChanges()
  }

  // Refresh button handler
  refresh() {
    this.getBohInfo()
  }

  // Triggered when department dropdown changes
  onDepartmentChange() {
    // Clear search whenever the department changes so users don't get confused
    this.search = ''
    this.applyFilters()
  }

  // Triggered when search input changes
  applyFilters() {
    let records = this.allBohRecords.slice()

    // Filter by department first (unless "All Departments")
    if (this.selectedDepartment !== this.ALL_DEPARTMENTS) {
      records = records.filter(r => r.departmentNumber === this.selectedDepartment)
    }

    // Then apply search within that department
    const term = this.search.trim().toLowerCase()
    if (term !== '') {
      records = records.filter(r =>
        (r.productName?.toLowerCase().includes(term)) ||
        (r.upcNumber?.includes(term))
      )
    }

    this.filteredRecords = records
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
    if (this.pageSize === 'ALL'){
      return 1;
    }
    return Math.max(1, Math.ceil(this.filteredRecords.length / this.pageSize))
  }

  get pagedRecords(): BohRecord[] {
    if (this.pageSize === 'ALL'){
      return this.filteredRecords;
    }
    const start = (this.currentPage - 1) * this.pageSize
    return this.filteredRecords.slice(start, start + this.pageSize)
  }

  // Used in template to decide which empty-state message to show
  get isSearching(): boolean {
    return this.search.trim() !== ''
  }

  addToCart(record: BohRecord){
    const qty = Number(this.pendingQuantities[record.upcNumber]);

    if (!qty || qty <= 0 || !Number.isInteger(qty)) {
      this.showMessage('Enter a valid quantity before adding to order.', false);
      return;
    }

    const existingItem = this.cartItems.find(item => item.upcNumber === record.upcNumber);

    if(existingItem){
      existingItem.quantity += qty;
    } else{
      this.cartItems.push({
        upcNumber: record.upcNumber,
        productName: record.productName,
        quantity: qty
      });
    }

    delete this.pendingQuantities[record.upcNumber];
    this.showMessage(`${record.productName} added to order.`, true);
  }

  removeFromCart(upcNumber: string){
    this.cartItems = this.cartItems.filter(item => item.upcNumber !== upcNumber);
  }

  clearCart(){
    this.cartItems = [];
    this.pendingQuantities = {};
  }

  async placeCartOrder(){
    const user = this.auth.user();
    if (!user) {
      this.showMessage('Failed to place order. You must be logged in.', false);
      return;
    }

    if (this.cartItems.length === 0) {
      this.showMessage('There are no items in the order.', false);
      return;
    }

    try{
      const products = this.cartItems.map(item => ({
        upcNumber: item.upcNumber,
        quantity: item.quantity
      }));

      const response = await this.api.placeOrder(
        user.storeNumber,
        user.divisionNumber,
        user.userEuid,
        products
      );

      if (response.responseCode === 200) {
        this.showMessage('Order placed successfully and is pending reception.', true);
        this.clearCart();
        await this.getBohInfo();
      } else {
        this.showMessage(response.responseMessage, false);
      }

    } catch (error: any) {
      console.error('Failed to place cart order:', error);

      if (error?.error?.responseCode && error?.error?.responseMessage) {
        this.showMessage(
          `Error: ${error.error.responseCode} ${error.error.responseMessage}`,
          false
        );
      } else {
        this.showMessage('Failed to place order.', false);
      }
    }

    this.cd.detectChanges();
  }

}
