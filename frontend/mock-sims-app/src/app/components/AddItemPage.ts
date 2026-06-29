import {Component, inject, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Api, AddItemResponse, DepartmentInfoRecord, GetMarkdownRuleRecord} from '../services/api';
import {ChangeDetectorRef} from '@angular/core';
import {AuthService} from '../services/auth';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-add-item-page',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './template/AddItemPageTemplate.html'
})

export class AddItemPage implements OnInit{
  private api = inject(Api);
  private auth = inject(AuthService);

  constructor(private cd: ChangeDetectorRef) {
  }

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

  departmentInfo: DepartmentInfoRecord[] = [];
  message: string = '';
  isSuccess: boolean = false;

  markdownRules: GetMarkdownRuleRecord[] = [];
  matchingSubcommodities: GetMarkdownRuleRecord[] = [];
  selectedExistingRule: GetMarkdownRuleRecord | null = null;
  isExistingSubcommodityRule: boolean = false;

  invalidUpcNumber: boolean = false;
  invalidSubcommodityNumber: boolean = false;
  invalidDepartmentNumber: boolean = false;
  invalidProductName: boolean = false;
  invalidStandardPrice: boolean = false;
  invalidMarkdownOrder: boolean = false;


  invalidFirstMarkdownPercent: boolean = false;
  invalidDaysBeforeExpToMD: boolean = false;
  invalidDaysBeforeExpToRFI: boolean = false;
  invalidDaysAfterOrderToSetExp: boolean = false;

  ngOnInit() {
    this.getDepartmentInfo();
    this.getMarkdownRules();
  }

  async getDepartmentInfo() {

    try {
      const response = await this.api.getDepartmentInfo();
      this.departmentInfo = response.departmentInfoRecords;
      console.log(this.departmentInfo);
    } catch (error) {
      this.showMessage('Failed to load department info. Error 503: Failed to contact server', false);
    }
    this.cd.detectChanges();
  }

  async getMarkdownRules() {
    try {
      const response = await this.api.getMarkdownRules();
      this.markdownRules = response.markdownRules || [];
      console.log(this.markdownRules);
    } catch (error) {
      this.markdownRules = [];
      this.showMessage('Failed to load markdown rules. Error 503: Failed to contact server', false);
    }

    this.cd.detectChanges();
  }

  showMessage(text: string, success: boolean) {
    this.message = text;
    this.isSuccess = success;

    setTimeout(() => {
      this.message = '';
      this.cd.detectChanges();
    }, 3000);
  }

  // Clear the validation flags after form submission
  resetValidationFlags() {
    this.invalidUpcNumber = false;
    this.invalidSubcommodityNumber = false;
    this.invalidDepartmentNumber = false;
    this.invalidProductName = false;
    this.invalidStandardPrice = false;

    this.invalidFirstMarkdownPercent = false;
    this.invalidDaysBeforeExpToMD = false;
    this.invalidDaysBeforeExpToRFI = false;
    this.invalidDaysAfterOrderToSetExp = false;
    this.invalidMarkdownOrder = false;
  }

  resetMarkdownValidationFlags() {
    this.invalidFirstMarkdownPercent = false;
    this.invalidDaysBeforeExpToMD = false;
    this.invalidDaysBeforeExpToRFI = false;
    this.invalidDaysAfterOrderToSetExp = false;
    this.invalidMarkdownOrder = false;
  }


  onSubcommodityInput() {
    const term = this.subcommodityNumber.trim();
    const previousWasExistingRule = this.isExistingSubcommodityRule;

    this.selectedExistingRule = null;
    this.isExistingSubcommodityRule = false;

    if (term === '') {
      this.matchingSubcommodities = [];
      this.clearMarkdownRuleFields();
      return;
    }

    this.matchingSubcommodities = this.markdownRules
      .filter(rule => rule.subcommodityNumber.includes(term))
      .slice(0, 10);

    const exactMatch = this.markdownRules.find(
      rule => rule.subcommodityNumber === term
    );

    if (exactMatch) {
      this.applyExistingMarkdownRule(exactMatch);
      this.matchingSubcommodities = [];
    } else if (previousWasExistingRule) {
      this.clearMarkdownRuleFields();
    }

    this.cd.detectChanges();
  }

  selectSubcommodityRule(rule: GetMarkdownRuleRecord) {
    this.subcommodityNumber = rule.subcommodityNumber;
    this.applyExistingMarkdownRule(rule);
    this.matchingSubcommodities = [];
    this.cd.detectChanges();
  }

  applyExistingMarkdownRule(rule: GetMarkdownRuleRecord) {
    this.selectedExistingRule = rule;
    this.isExistingSubcommodityRule = true;

    this.canBeMarkedDown = rule.canBeMarkedDown;
    this.firstMarkdownPercent = rule.firstMarkdownPercent;
    this.daysBeforeExpToMD = rule.daysBeforeExpToMD;
    this.daysBeforeExpToRFI = rule.daysBeforeExpToRFI;
    this.daysAfterOrderToSetExp = rule.daysAfterOrderToSetExp;

    this.resetMarkdownValidationFlags();
  }

  clearMarkdownRuleFields() {
    this.selectedExistingRule = null;
    this.isExistingSubcommodityRule = false;

    this.canBeMarkedDown = false;
    this.firstMarkdownPercent = null;
    this.daysBeforeExpToMD = null;
    this.daysBeforeExpToRFI = null;
    this.daysAfterOrderToSetExp = null;

    this.resetMarkdownValidationFlags();
  }

  // Clears the mark-down related fields if the item cannot be marked down
  onCanBeMarkedDownChange() {
    if (this.isExistingSubcommodityRule) {
      return;
    }

    if (!this.canBeMarkedDown) {
      this.firstMarkdownPercent = null;
      this.daysBeforeExpToMD = null;
      this.daysBeforeExpToRFI = null;
      this.daysAfterOrderToSetExp = null;

      this.resetMarkdownValidationFlags();
    }
  }

  // Validates the required fields
  validateForm(): boolean {
    this.resetValidationFlags();

    let isValid = true;

    // This is for value specification
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

    if (this.standardPrice === null || this.standardPrice <= 0) {
      this.invalidStandardPrice = true;
      isValid = false;
    }


    if (this.canBeMarkedDown) {
      if (this.firstMarkdownPercent === null || this.firstMarkdownPercent <= 0) {
        this.invalidFirstMarkdownPercent = true;
        isValid = false;
      }

      if (this.daysBeforeExpToMD === null || this.daysBeforeExpToMD < 0) {
        this.invalidDaysBeforeExpToMD = true;
        isValid = false;
      }

      if (this.daysBeforeExpToRFI === null || this.daysBeforeExpToRFI < 0) {
        this.invalidDaysBeforeExpToRFI = true;
        isValid = false;
      }

      if (this.daysAfterOrderToSetExp === null || this.daysAfterOrderToSetExp < 0) {
        this.invalidDaysAfterOrderToSetExp = true;
        isValid = false;
      }

      // Only evaluate ordering rule if all three fields exist
      if (
        this.daysAfterOrderToSetExp !== null &&
        this.daysBeforeExpToMD !== null &&
        this.daysBeforeExpToRFI !== null
      ) {
        if (!(this.daysAfterOrderToSetExp > this.daysBeforeExpToMD && this.daysBeforeExpToMD > this.daysBeforeExpToRFI)
        ) {
          this.invalidDaysAfterOrderToSetExp = true;
          this.invalidDaysBeforeExpToMD = true;
          this.invalidDaysBeforeExpToRFI = true;
          this.invalidMarkdownOrder = true;
          isValid = false;
        }
      }
    }

    return isValid;
  }

  async addItemAction() {
    let addItemResponse: AddItemResponse;
    const user = this.auth.user();

    if (!user) {
      this.showMessage('Failed to add item. You must be logged in to add an item.', false);
      return;
    }

    // Frontend validation
    if (!this.validateForm()) {
      if (this.invalidMarkdownOrder) {
        this.showMessage(
          'Markdown day values must follow this order: Days After Order To Set Expiration > Days Before Expiration To Markdown > Days Before Expiration To RFI.',
          false
        );
      } else if (this.canBeMarkedDown &&
        (this.invalidFirstMarkdownPercent ||
          this.invalidDaysBeforeExpToMD ||
          this.invalidDaysBeforeExpToRFI ||
          this.invalidDaysAfterOrderToSetExp)) {
        this.showMessage(
          'If Can Be Marked Down is checked, all markdown-related fields are required.',
          false
        );
      } else {
        this.showMessage('Please fill in all required fields.', false);
      }
      return;
    }

    try {
      addItemResponse = await this.api.addItem(
        user.storeNumber,
        user.divisionNumber,
        this.upcNumber,
        this.subcommodityNumber,
        this.departmentNumber,
        this.productName,
        this.standardPrice!,
        this.firstMarkdownPercent,
        this.canBeMarkedDown,
        this.daysBeforeExpToMD,
        this.daysBeforeExpToRFI,
        this.daysAfterOrderToSetExp
      );
    } catch (error: any) {
      console.error('Add Item error:', error);

      if (error?.error?.responseCode && error?.error?.responseMessage) {
        addItemResponse = {
          responseCode: error.error.responseCode,
          responseMessage: error.error.responseMessage
        };
      } else {
        addItemResponse = {
          responseCode: 503,
          responseMessage: 'Failed to contact server'
        };
      }
    }

    if (addItemResponse.responseCode === 200) {
      this.showMessage(addItemResponse.responseMessage, true);
      this.clearFormAfterSuccess();
      await this.getMarkdownRules();
      this.resetValidationFlags();

    } else {
      this.showMessage(addItemResponse.responseMessage, false);
    }
    this.cd.detectChanges();
  }


  clearFormAfterSuccess() {
    // Clears the form fields after success
    this.upcNumber = '';
    this.subcommodityNumber = '';
    this.departmentNumber = '';
    this.productName = '';
    this.standardPrice = null;
    this.firstMarkdownPercent = null;
    this.canBeMarkedDown = false;
    this.daysBeforeExpToMD = null;
    this.daysBeforeExpToRFI = null;
    this.daysAfterOrderToSetExp = null;
    this.matchingSubcommodities = [];
    this.selectedExistingRule = null;
    this.isExistingSubcommodityRule = false;
  }
}
