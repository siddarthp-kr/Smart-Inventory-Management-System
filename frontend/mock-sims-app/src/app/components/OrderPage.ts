import {Component, inject, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Api, OrderHistoryRecord, OrderHistoryResponse, PlaceOrderResponse} from '../services/api';
import { ChangeDetectorRef } from '@angular/core';
import {AuthService} from '../services/auth';
import {Router, RouterLink} from '@angular/router';


@Component({
  selector: 'app-order-page',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './template/OrderPageTemplate.html'
})
export class OrderPage {

  ngOnInit() {
    this.searchFunction()
  }

  //Store what is typed in search
  search: string = '';
  message: string = '';
  results: any[] = [];
  isSuccess: boolean = false;

  private api = inject(Api)
  private auth = inject(AuthService)
  private router = inject(Router);
  constructor(private cd: ChangeDetectorRef){}

  //Hard-coded Products - (4-Departments)
  products = [
    //Produce
    {upc: '4011', name: 'Banana', subCommodity: '62000'},
    {upc: '4022', name: 'ST Cashews', subCommodity: '64307'},

    //Deli/Bakery
    {upc: '3011', name: 'Feta Greek', subCommodity: '44805'},
    {upc: '3022', name: 'Shortbread Butter Cookies', subCommodity: '64307'},

    //Meat
    {upc: '1011', name: 'Chicken Breast Boneless', subCommodity: '53000'},
    {upc: '1022', name: 'KRO GRND BF Burger', subCommodity: '56005'},

    //Grocery
    {upc: '2011', name: 'Merry Edwards Pinot Noir', subCommodity: '04703'},
    {upc: '2022', name: 'LA PREF Garbanzo Chickpeas', subCommodity: '98418'},
    {upc: '2033', name: 'Motts Fruit Animal Snacks', subCommodity: '27602'},
    {upc: '2044', name: 'Honey Bunches Oats', subCommodity: '29465'}
  ];

  // Triggers the search when button clicked
  searchFunction(){
    const feature = this.search.trim().toLowerCase();
    //checking filter for each product
    this.results = this.products.filter(
      // Checking to see if the name is contained within the search
      p => p.name.toLowerCase().includes(feature) ||
        // Checking to see if the upc is contained within the search
        p.upc.includes(feature)
    );
  }

  showMessage(text: string, success: boolean){
    this.message = text;
    this.isSuccess = success;

    //How long the message should appear before disappearing
    setTimeout(() => {
      this.message = '';
    }, 3000);
  }

  // Create order action (mock version)
  // Triggers order action when button clicked
  async orderAction(product: any, quantity: number, messageInput: any){
    const user = this.auth.user()
    // Quantity must be valid to proceed
    if(!user){
      this.showMessage('Failed to place order. You must be logged in to place an order', false);
      return;
    }
    if(!quantity || quantity <= 0){
      this.showMessage ('Enter a valid quantity', false);
      return;
    }

    // do extra validation here
    let orderResponse: PlaceOrderResponse
    try {
      orderResponse = await this.api.placeOrder(user.storeNumber, user.divisionNumber, user.userEuid, product.upc, quantity);
    } catch (error){
      orderResponse = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        orderId: -1
      }
    }

    let successOrder = false
    if(orderResponse.responseCode === 200){
      successOrder = true
    }

    // Displays message at top when placing an order including quantity and product name
    if (successOrder){
      this.showMessage(`Order placed successfully! Product: ${product.name} | Quantity: ${quantity} | Order ID: ${orderResponse.orderId}`, true);
    } else{
      this.showMessage(`Failed to place order for ${product.name}. Error: ${orderResponse.responseCode} ${orderResponse.responseMessage}`, false);
    }
    this.cd.detectChanges()
    // Allows for clearing any input when order is placed
    messageInput.value = '';
  }

}

