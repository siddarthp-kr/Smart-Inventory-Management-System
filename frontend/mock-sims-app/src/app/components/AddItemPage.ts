import {Component, inject} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Api, AddItemResponse} from '../services/api';
import {ChangeDetectorRef} from '@angular/core';
import {AuthService} from '../services/auth';

@Component({
  selector: 'app-add-item-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './template/AddItemPageTemplate.html'
})

export class AddItemPage{
  private api = inject(Api);
  private auth = inject(AuthService);
  constructor(private cd: ChangeDetectorRef) {}

  upcNumber: string = '';
  subcommodityNumber: string = '';
  departmentNumber: string = '';
  productName: string = '';
  standardPrice: number | null = null;
  firstMarkdownPercent: number | null = null;
  canBeMarkedDown: boolean = false;
  daysBeforeExpToMD: number | null = null;
  daysBeforeExpToRFI: number | null = null;
  daysAfterOrderToSetExp: number | null = null;

  // UI response
  message: string = '';
  isSuccess: boolean = false;

  invalidUpcNumber: boolean = false;
  invalidSubcommodityNumber: boolean = false;
  invalidDepartmentNumber: boolean = false;
  invalidProductName: boolean = false;
  invalidStandardPrice: boolean = false;

  showMessage(text: string, success: boolean){
    this.message = text;
    this.isSuccess = success;

    setTimeout(() => {
      this.message = '';
    }, 3000);
  }
  // Clear the validation flags after form submission
  resetValidationFlags(){
    this.invalidUpcNumber = false;
    this.invalidSubcommodityNumber = false;
    this.invalidDepartmentNumber = false;
    this.invalidProductName = false;
    this.invalidStandardPrice = false;
  }

  // Validating the required fields
  validateForm(): boolean {
    this.resetValidationFlags();

    let isValid = true;

    // Required + specific expected values
    if (!this.upcNumber || this.upcNumber.trim() === '') {
      this.invalidUpcNumber = true;
      isValid = false;
    }

    if (!this.subcommodityNumber || this.subcommodityNumber.trim() === '') {
      this.invalidSubcommodityNumber = true;
      isValid = false;
    }

    if (!this.departmentNumber || this.departmentNumber.trim() === '') {
      this.invalidDepartmentNumber = true;
      isValid = false;
    }

    if (!this.productName || this.productName.trim() === '') {
      this.invalidProductName = true;
      isValid = false;
    }

    if (!this.standardPrice || this.standardPrice <= 0) {
      this.invalidStandardPrice = true;
      isValid = false;
    }

    return isValid;
  }


  async addItemAction() {
    let addItemResponse: AddItemResponse;
    const user = this.auth.user();

    if (!user){
      this.showMessage('Failed to add item. You must be logged in to add an item.', false);
      return;
    }

    // Frontend validation
    if (!this.validateForm()){
      this.showMessage('Please fill in all required fields.', false);
      return;
    }

    try{
      addItemResponse = await this.api.addItem(
        user.storeNumber,
        user.divisionNumber,
        this.upcNumber,
        this.subcommodityNumber,
        this.departmentNumber,
        this.productName,
        this.standardPrice!,
        this.firstMarkdownPercent ?? 0,
        this.canBeMarkedDown ?? 0,
        this.daysBeforeExpToMD ?? 0,
        this.daysBeforeExpToRFI ?? 0,
        this.daysAfterOrderToSetExp ?? 0
      );
    } catch (error: any){
      console.error('Add Item error:', error);

      if (error?.error?.responseCode && error?.error?.responseMessage){
        addItemResponse = {
          responseCode: error.error.responseCode,
          responseMessage: error.error.responseMessage
        };
      }
      else{
        addItemResponse = {
          responseCode: 503,
          responseMessage: 'Failed to contact server'
        };
      }
    }

    console.log(addItemResponse);

    if (addItemResponse.responseCode === 200){
      this.showMessage(`Added item ${this.productName} successfully`, true);

      // Clears the form fields after success
      this.upcNumber = '';
      this.subcommodityNumber = '';
      this.departmentNumber = '';
      this.productName = '';
      this.standardPrice = 0;
      this.firstMarkdownPercent = 0;
      this.canBeMarkedDown = false;
      this.daysBeforeExpToMD = 0;
      this.daysBeforeExpToRFI = 0;
      this.daysAfterOrderToSetExp = 0

      this.resetValidationFlags()
    }
    else {
      this.showMessage( `${addItemResponse.responseMessage}`, false);
    }

    this.cd.detectChanges();
  }
}
