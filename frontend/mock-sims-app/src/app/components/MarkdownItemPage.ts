import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth';
import {Api} from '../services/api';


interface MarkdownItemState {
  alertId: number,
  upcNumber: string
}

@Component({
  templateUrl: './template/MarkdownItemPageTemplate.html',
  selector: 'markdown-item-page',
  standalone: true
})
export class MarkdownItemPage {

  private alertId = 0
  private upcNumber = ''

  private missingState = false

  constructor(private cd: ChangeDetectorRef) {
    const nav = this.router.getCurrentNavigation()
    const state = nav?.extras?.state as MarkdownItemState | undefined

    if (!state || state.alertId == null) {
      // User landed here without valid state — go back
      this.missingState = true
      return
    }

    this.alertId = state.alertId
    this.upcNumber = state.upcNumber;

  }

  private router = inject(Router)
  private auth = inject(AuthService)
  private api = inject(Api)

  private originalPrice = 0
  private newPrice = 0

  ngOnInit(){
    if (this.missingState) {
      this.router.navigate(['/AlertsPage'])
    }

    this.getMarkdownInfo(this.upcNumber, this.alertId)

  }

  async getMarkdownInfo(upcNumber: string, alertId: number){
    try {
      const markdownInfo = await this.api.getMarkdownInfo(this.upcNumber, this.alertId)
      this.originalPrice = markdownInfo.originalPrice
      this.newPrice = markdownInfo.newPrice
    } catch (error){

    }
  }
}
