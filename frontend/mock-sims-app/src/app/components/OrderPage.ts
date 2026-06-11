import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-order-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './template/OrderPageTemplate.html'
})
export class OrderPage {
  search: string = '';
  results: any[] = [];

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
    {upc: '3033', name: 'Motts Fruit Animal Snacks', subCommodity: '27602'},
    {upc: '3044', name: 'Honey Bunches Oats', subCommodity: '29465'}
  ];

  // Creating the search function to be used
  searchFunction(){
    const feature = this.search.toLowerCase();
    this.results = this.products.filter(
      p => p.name.toLowerCase().includes(feature) ||
        p.upc.includes(feature)
    );
  }

  // Create order action (mock version)
  orderAction(product: any, quantity: number){
    if(!quantity || quantity <= 0){
      alert("Enter a valid quantity");
      return;
    }
  }

}

