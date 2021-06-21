import {Component, OnInit} from '@angular/core';
import {I18nService} from "@dcc-commons-ng/services";
import {EAuthority} from "@shared/model";
import {AuthenticationService} from "@core/services";

@Component({
  selector: 'app-printing-request-details',
  templateUrl: './printing-request-details.component.html',
  styleUrls: ['./printing-request-details.component.scss']
})
export class PrintingRequestDetailsComponent implements OnInit {
  public isPackageOne= false;
  public isPackageTwo= false;
  constructor(private i18nService: I18nService,
              private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  get canSeePrintRequestDetails(): boolean {
    //TODO: change it to PRINTING_MANAGEMENT
    return this.authenticationService.hasAuthority(EAuthority.USER_MANAGEMENT);
  }

}
