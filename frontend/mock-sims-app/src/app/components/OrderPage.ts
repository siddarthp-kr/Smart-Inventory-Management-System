import {Component, inject, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {Api, PlaceOrderResponse, ProductItem, ProductsResponse} from '../services/api';
import { ChangeDetectorRef } from '@angular/core';
import {AuthService} from '../services/auth';


@Component({
  selector: 'app-order-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './template/OrderPageTemplate.html'
})
export class OrderPage implements OnInit{

  //Store what is typed in search
  search: string = '';
  message: string = '';
  results: ProductItem[] = [];
  isSuccess: boolean = false;
  products: ProductItem[] = [];

  private api = inject(Api)
  private auth = inject(AuthService)
  constructor(private cd: ChangeDetectorRef){}

  async ngOnInit(){
    await this.loadProducts();
    this.searchFunction()
  }


  async loadProducts() {
    const user = this.auth.user();

    if (!user) {
      this.showMessage('You must be logged in to load products.', false);
      return;
    }

    try {
      const productResponse: ProductsResponse = await this.api.getProducts(
        user.storeNumber,
        user.divisionNumber
      );

      this.products = productResponse.products;
      this.results = this.products;

    } catch (error) {
      console.error('Failed to load products:', error);
      this.showMessage('Failed to load products from server.', false);
    }

    this.cd.detectChanges();
  }

  // Triggers the search when button clicked
  searchFunction(){
    const feature = this.search.trim().toLowerCase();
    //checking filter for each product
    this.results = this.products.filter(
      // Checking to see if the name is contained within the search
      p => p.productName.toLowerCase().includes(feature) ||
        // Checking to see if the upc is contained within the search
        p.upcNumber.includes(feature)
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
  async orderAction(product: ProductItem, quantity: number, messageInput: any){
    const user = this.auth.user()
    console.log(user)
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
      orderResponse = await this.api.placeOrder(user.storeNumber, user.divisionNumber, user.userEuid, product.upcNumber, quantity);
    } catch (error){
      orderResponse = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        orderId: -1
      }
    }

    console.log(orderResponse)

    let successOrder = false
    if(orderResponse.responseCode === 200){
      successOrder = true
    }

    // Displays message at top when placing an order including quantity and product name
    if (successOrder){
      this.showMessage(`Order placed successfully! Product: ${product.productName} | Quantity: ${quantity} | Order ID: ${orderResponse.orderId}`, true);
    } else{
      this.showMessage(`Failed to place order for ${product.productName}. Error: ${orderResponse.responseCode} ${orderResponse.responseMessage}`, false);
    }
    this.cd.detectChanges()
    // Allows for clearing any input when order is placed
    messageInput.value = '';
  }
}

