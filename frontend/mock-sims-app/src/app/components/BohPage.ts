import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api, BohRecord, DepartmentInfoRecord } from '../services/api';
import { AuthService } from '../services/auth';


@Component({
  selector: 'app-boh-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
  pageSizeOptions: number[] = [5, 10, 50, 100, 500]
  pageSize: number = 10
  currentPage: number = 1

  // Status message (same pattern as other pages)
  statusMessage: string = ''
  statusIsSuccess: boolean = true

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
    return Math.max(1, Math.ceil(this.filteredRecords.length / this.pageSize))
  }

  get pagedRecords(): BohRecord[] {
    const start = (this.currentPage - 1) * this.pageSize
    return this.filteredRecords.slice(start, start + this.pageSize)
  }

  // Used in template to decide which empty-state message to show
  get isSearching(): boolean {
    return this.search.trim() !== ''
  }
}
