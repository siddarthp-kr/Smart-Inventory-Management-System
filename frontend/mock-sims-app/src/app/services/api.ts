import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import {firstValueFrom} from 'rxjs';

export interface BohRecord {
  upcNumber: string,
  qodNumber: number,
  qomNumber: number,
  departmentName: string,
  departmentNumber: string,
  productName: string
}

export interface BohResponse {
  products: BohRecord[]
}

export interface PlaceOrderProduct{
  upcNumber: string;
  quantity: number;
}

export interface PlaceOrderResponse {
  responseCode: number,
  responseMessage: string,
  orderId: number
}

export interface ReceiveOrderResponse {
  responseCode: number;
  responseMessage: string;
}

export interface DepartmentInfoRecord {
  departmentNumber: string,
  departmentName: string
}

export interface DepartmentInfoResponse {
  departmentInfoRecords: DepartmentInfoRecord[]
}

export interface OrderHistoryRecord {
  orderId: number;
  placedByUserEuid: string;
  orderPlacedTime: string;
  orderReceived: boolean;
  receivedByUserEuid: string | null;
  orderReceivedTime: string | null;
  upcNumber: string;
  productName: string;
  quantity: number;
}

export interface OrderHistoryResponse {
  responseCode: number,
  responseMessage: string,
  orders: Array<OrderHistoryRecord>
}

export interface MovementInfoRecord {
  upcNumber: string;
  productName: string;
  movementType: string;
  userEuid: string;
  qodBeforeTransaction: number | null;
  qomBeforeTransaction: number | null;
  actionTime: string;
  quantityChanged: number;
  sourceBucket: string | null;
  reasonCode: string | null;
  originalPrice: number | null;
  newPrice: number | null;
}

export interface MovementInfoResponse{
  responseCode: number;
  responseMessage: string;
  movements: MovementInfoRecord[];
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

export interface GetPdmAlertRecord {
  alertId: number;
  departmentNumber: string;
  upcNumber: string;
  expirationDate: string;
  mdAfterDate: string;
  rfiAfterDate: string;
}

export interface GetPdmAlertsResponse {
  responseMessage: string;
  pdmAlerts: GetPdmAlertRecord[];
}

export interface GetMarkdownRuleRecord {
  subcommodityNumber: string;
  firstMarkdownPercent: number | null;
  canBeMarkedDown: boolean;
  daysBeforeExpToMD: number | null;
  daysBeforeExpToRFI: number | null;
  daysAfterOrderToSetExp: number | null;
}

export interface GetMarkdownRulesResponse {
  responseMessage: string;
  markdownRules: GetMarkdownRuleRecord[];
}

@Injectable({ providedIn: 'root' })
export class Api {
  private http = inject(HttpClient);

  async getPdmAlerts(storeNumber: string, divisionNumber: string): Promise<GetPdmAlertsResponse> {
    const result: GetPdmAlertsResponse = await firstValueFrom(
      this.http.get<GetPdmAlertsResponse>(
        `http://localhost:8080/api/pdm/get-pdm-alerts?storeNumber=${storeNumber}&divisionNumber=${divisionNumber}`
      )
    );

    return result;
  }

  async placeOrder(storeNumber: string, divisionNumber: string, userEuid: string, products: PlaceOrderProduct[]): Promise<PlaceOrderResponse> {
    const requestBody = {
      storeNumber,
      divisionNumber,
      userEuid,
      items: products
    };


    //do validation to make sure that the request is valid
    let result: PlaceOrderResponse = await firstValueFrom(
      this.http.post<PlaceOrderResponse>('http://localhost:8080/api/order/place-order', requestBody)
    )

    return result;
  }

  async receiveOrder(storeNumber: string, divisionNumber: string, userEuid: string, orderId: number): Promise<ReceiveOrderResponse> {
    const requestBody = {
      storeNumber,
      divisionNumber,
      userEuid,
      orderId
    };

    const result: ReceiveOrderResponse = await firstValueFrom(
      this.http.post<ReceiveOrderResponse>(
        'http://localhost:8080/api/order/receive-order', requestBody)
    )

    return result;
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

  async getDepartmentInfo(): Promise<DepartmentInfoResponse>{
    let result: DepartmentInfoResponse = await firstValueFrom(
      this.http.get<any>('http://localhost:8080/api/boh/department-info')
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

  async getBohInfo(storeNumber: string, divisionNumber: string): Promise<BohResponse> {
    let result: BohResponse = await firstValueFrom(
      this.http.get<any>(`http://localhost:8080/api/boh/get-boh-count?storeNumber=${storeNumber}&divisionNumber=${divisionNumber}`)
    )
    return result
  }

  async getMovementInfo(storeNumber: string, divisionNumber: string, upcNumber: string,): Promise<MovementInfoResponse> {
    const result: MovementInfoResponse = await firstValueFrom(

      this.http.get<MovementInfoResponse>(`http://localhost:8080/api/boh/movement-info?storeNumber=${storeNumber}&divisionNumber=${divisionNumber}&upcNumber=${upcNumber}`)
    );
    return result;
  }

  async getMarkdownRules(): Promise<GetMarkdownRulesResponse> {
    const result: GetMarkdownRulesResponse = await firstValueFrom(
      this.http.get<GetMarkdownRulesResponse>('http://localhost:8080/api/order/markdown-rules')
    );
    return result;
  }
}
