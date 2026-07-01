import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  Api,
  DepartmentInfoRecord,
  GetPdmAlertRecord,
  ProductItem,
  ProductsResponse
} from '../services/api';
import { AuthService } from '../services/auth';
import {Router} from '@angular/router';


interface PdmAlertDisplayRecord extends GetPdmAlertRecord {
  productName: string;
  departmentName: string;
}

interface AlertsPageState {
  departmentNumber: string
}

@Component({
  selector: 'app-pdm-alerts-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './template/AlertsPageTemplate.html'
})
export class AlertsPage implements OnInit {
  private api = inject(Api)
  private auth = inject(AuthService)
  private router = inject(Router);

  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation()
    const state = nav?.extras?.state as AlertsPageState | undefined


    if(!state || !state.departmentNumber){
      return
    }

    this.departmentNumber = state.departmentNumber;

  }

  departmentNumber: string = ''

  // Department dropdown data
  departmentInfo: DepartmentInfoRecord[] = []

  // Product info used to attach productName to each alert
  products: ProductItem[] = []

  // All PDM alerts from backend, enriched with product/department display info
  allAlerts: PdmAlertDisplayRecord[] = []

  // Alerts after department + search filtering
  filteredAlerts: PdmAlertDisplayRecord[] = []

  // Dropdown state
  readonly ALL_DEPARTMENTS = ''

  selectedDepartment: string = this.ALL_DEPARTMENTS

  // Search input
  search: string = ''

  // Status message
  statusMessage: string = ''
  statusIsSuccess: boolean = true

  ngOnInit() {
    this.loadPdmAlertsPage()
    this.selectedDepartment = this.departmentNumber
  }

  showMessage(text: string, success: boolean) {
    this.statusMessage = text
    this.statusIsSuccess = success

    setTimeout(() => {
      this.statusMessage = ''
    }, 3000)
  }

  async loadPdmAlertsPage() {
    const user = this.auth.user()

    if (!user) {
      this.showMessage('Failed to load PDM alerts. You must be logged in.', false)
      return
    }

    try {
      const departmentResponse = await this.api.getDepartmentInfo()
      this.departmentInfo = departmentResponse.departmentInfoRecords || []

      const productResponse: ProductsResponse = await this.api.getProducts(
        user.storeNumber,
        user.divisionNumber
      )
      this.products = productResponse.products || []

      const pdmAlertsResponse = await this.api.getPdmAlerts(
        user.storeNumber,
        user.divisionNumber
      )

      this.allAlerts = this.buildDisplayAlerts(pdmAlertsResponse.pdmAlerts || [])
      this.applyFilters()
    } catch (error) {
      this.allAlerts = []
      this.filteredAlerts = []
      this.showMessage('Failed to load PDM alerts. Error 503: Failed to contact server', false)
    }

    this.cd.detectChanges()
  }

  private buildDisplayAlerts(alerts: GetPdmAlertRecord[]): PdmAlertDisplayRecord[] {
    const productNameByUpc = new Map<string, string>()
    for (const product of this.products) {
      productNameByUpc.set(product.upcNumber, product.productName)
    }

    const departmentNameByNumber = new Map<string, string>()
    for (const department of this.departmentInfo) {
      departmentNameByNumber.set(department.departmentNumber, department.departmentName)
    }

    return alerts.map(alert => ({
      ...alert,
      productName: productNameByUpc.get(alert.upcNumber) || 'Unknown Product',
      departmentName: departmentNameByNumber.get(alert.departmentNumber) || ''
    }))
  }

  refresh() {
    this.loadPdmAlertsPage()
  }

  onDepartmentChange() {
    this.search = ''
    this.applyFilters()
  }

  applyFilters() {
    let alerts = this.allAlerts.slice()

    if (this.selectedDepartment !== this.ALL_DEPARTMENTS) {
      alerts = alerts.filter(alert => alert.departmentNumber === this.selectedDepartment)
    }

    const term = this.search.trim().toLowerCase()

    if (term !== '') {
      alerts = alerts.filter(alert =>
        alert.productName.toLowerCase().includes(term) ||
        alert.upcNumber.includes(term)
      )
    }

    this.filteredAlerts = alerts
  }

  get isSearching(): boolean {
    return this.search.trim() !== ''
  }

  takeAction(alert: PdmAlertDisplayRecord) {

    this.router.navigate(['/AlertActionPage'], {
      state: {
        alertId: alert.alertId,
        upcNumber: alert.upcNumber,
        productName: alert.productName,
        mdBeforeDate: alert.mdBeforeDate,
        rfiBeforeDate: alert.rfiBeforeDate,
        expirationDate: alert.expirationDate,
        departmentNumber: this.selectedDepartment
      }
    })
  }
}
