import {Component, OnInit} from '@angular/core';
import {EAuthority, Page} from "@shared/model";
import {AuthenticationService} from "@core/services";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-printing-request-list',
  templateUrl: './printing-request-list.component.html',
  styleUrls: ['./printing-request-list.component.scss']
})
export class PrintingRequestListComponent implements OnInit {
  public isSearchbarCollapsed= false;
  pageArray: Array<number>;
  page: Page;
  searchForm: FormGroup;
  canCreateNewRequest: boolean;

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.canCreateNewRequest = true;
  }

  private initForm(): void {

  }

  get canSeePrintRequestsList(): boolean {
    //TODO: change it to PRINTING_MANAGEMENT
    return this.authenticationService.hasAuthority(EAuthority.USER_MANAGEMENT);
  }

  search(): void {
  }

  cancelSearch() {
    this.initForm();
  }

}
