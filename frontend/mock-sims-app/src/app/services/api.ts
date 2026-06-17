import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

export interface PlaceOrderResponse {
  responseCode: number,
  responseMessage: string
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

    console.log(result)
    return result
  }
}
