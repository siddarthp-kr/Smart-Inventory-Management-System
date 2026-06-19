import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import {firstValueFrom} from 'rxjs';

export interface PlaceOrderResponse {
  responseCode: number,
  responseMessage: string,
  orderId: number
}


export interface OrderHistoryRecord {
  orderId: number,
  userEuid: string,
  orderPlacedDate: string,
  upcNumber: string,
  productName: string,
  quantity: number
}

export interface OrderHistoryResponse {
  responseCode: number,
  responseMessage: string,
  orders: Array<OrderHistoryRecord>
}

export interface AddItemResponse{
  responseCode: number;
  responseMessage: string;
}
export interface ProductItem{
  upcNumber: string;
  productName: string;
}
export interface ProductsResponse{
  products: ProductItem[];
}
@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);



  async placeOrder(storeNumber: string, divisionNumber: string, userEuid: string, upcNumber: string, quantity: number): Promise<PlaceOrderResponse>{
    const requestBody = {
      storeNumber: storeNumber,
      divisionNumber: divisionNumber,
      userEuid: userEuid,
      upcNumber: upcNumber,
      quantity: quantity
    }


    //do validation to make sure that the request is valid
    let result: PlaceOrderResponse = await firstValueFrom(
      this.http.post<PlaceOrderResponse>('http://localhost:8080/api/order/place-order', requestBody)
    )

    return result
  }

  async getOrderHistory(storeNumber: string, divisionNumber: string): Promise<OrderHistoryResponse>{
    const requestBody = {
      storeNumber: storeNumber,
      divisionNumber: divisionNumber
    }

    let result: OrderHistoryResponse = await firstValueFrom(
      this.http.post<any>('http://localhost:8080/api/order/order-history', requestBody)
    )

    return result;
  }

  async addItem(storeNumber: string, divisionNumber: string, upcNumber: string, subcommodityNumber: string, departmentNumber: string, productName: string, standardPrice: number, firstMarkdownPercent: number | null, canBeMarkedDown: boolean, daysBeforeExpToMD: number | null, daysBeforeExpToRFI: number | null, daysAfterOrderToSetExp: number| null): Promise<AddItemResponse>{
    const requestBody = {
      storeNumber: storeNumber,
      divisionNumber: divisionNumber,
      upcNumber: upcNumber,
      subcommodityNumber: subcommodityNumber,
      departmentNumber: departmentNumber,
      productName: productName,
      standardPrice: standardPrice,
      firstMarkdownPercent: firstMarkdownPercent,
      canBeMarkedDown: canBeMarkedDown,
      daysBeforeExpToMD: daysBeforeExpToMD,
      daysBeforeExpToRFI: daysBeforeExpToRFI,
      daysAfterOrderToSetExp: daysAfterOrderToSetExp
    };

    let result: AddItemResponse = await firstValueFrom(
      this.http.post<AddItemResponse>('http://localhost:8080/api/order/add-item', requestBody)
    );

    return result;
  }


  async getProducts(storeNumber: string, divisionNumber: string): Promise<ProductsResponse> {
    let result: ProductsResponse = await firstValueFrom(
      this.http.get<ProductsResponse>(`http://localhost:8080/api/order/products?storeNumber=${storeNumber}&divisionNumber=${divisionNumber}`)
    );

    console.log(result);
    return result;
  }
}
