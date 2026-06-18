import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {Api, OrderHistoryResponse} from '../services/api';
import { AuthService } from '../services/auth';
import {Router} from '@angular/router';


@Component({
  templateUrl: `./template/OrderHistoryPageTemplate.html`,
  selector: ``,
  imports: [],
  standalone: true
})
export class OrderHistoryPage {
  private auth = inject(AuthService)
  private api = inject(Api)
  private router = inject(Router)
  constructor(private cd: ChangeDetectorRef){}

  private statusMessage = ''
  private statusIsSuccess = true

  showMessage(message: string, status: boolean){
    this.statusMessage = message;
    this.statusIsSuccess = status;

    //How long the message should appear before disappearing
    setTimeout(() => {
      this.statusMessage = '';
    }, 3000);
  }

  async orderHistoryAction(){

    const user = this.auth.user()

    let orderHistoryResponse: OrderHistoryResponse

    if(!user){
      //this.showMessage('Failed to open order history. You must be logged in to see order history.', false)
      console.log('Failed to open order history. You must be logged in to see order history.')
      return
    }

    try {
      orderHistoryResponse = await this.api.getOrderHistory(user.storeNumber, user.divisionNumber)
    }  catch (error){
      orderHistoryResponse = {
        responseCode: 503,
        responseMessage: 'Failed to contact server',
        orderHistoryRecords: []
      }
      //this.showMessage(`Failed to open order history. Error: ${orderHistoryResponse.responseCode} ${orderHistoryResponse.responseMessage}`, false)
      console.log('Failed to open order history. Error: ${orderHistoryResponse.responseCode} ${orderHistoryResponse.responseMessage}')
    }


  }
}
