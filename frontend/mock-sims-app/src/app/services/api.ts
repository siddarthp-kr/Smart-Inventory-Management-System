import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { firstValueFrom } from 'rxjs';

interface PlaceOrderResponse {
  responseCode: number,
  responseMessage: string
}

export class Api {
  private http = inject(HttpClient);



  async placeOrder(storeNumber: string, divisionNumber: string, userEuid: string, upcNumber: string, quantity: number): Promise<PlaceOrderResponse>{
    //do validation to make sure that the request is valid
    let result = await firstValueFrom(
      this.http.post('http://localhost:8080/api/order/place-order', )
    )
  }
}
