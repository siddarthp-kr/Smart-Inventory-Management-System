import {Component, inject} from '@angular/core';
import { Api, DepartmentInfoRecord } from '../services/api';

@Component({
  selector: 'app-boh-page',
  standalone: true,
  templateUrl: './template/BohPageTemplate.html'
})
export class BohPage {
  private api = inject(Api)
  protected departmentInfo: DepartmentInfoRecord[] = []

  ngOnInit() {
    this.getDepartmentInfo()
  }

  async getDepartmentInfo(){
    try {
      this.departmentInfo = await this.api.getDepartmentInfo()
      console.log(this.departmentInfo)
    } catch(error) {
      console.log("Error 503: Failed to contact server")
    }
  }

}
