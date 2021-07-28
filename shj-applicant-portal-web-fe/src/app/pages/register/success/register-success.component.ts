import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {AuthenticationService} from '@app/_core/services/authentication/authentication.service';
import {I18nService} from "@dcc-commons-ng/services";


@Component({
  selector: 'app-register-success',
  templateUrl: './register-success.component.html',
  styleUrls: ['./register-success.component.scss']
})
export class RegisterSuccessComponent implements OnInit {

  recaptcha: any = null;

  dateString: string;

  constructor(
    private formBuilder: FormBuilder,
    private i18nService: I18nService,
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {

    // redirect to home if already logged in
    // if (this.authenticationService.isAuthenticated()) {
    //   this.router.navigate(['/']);
    // }
    alert("+++++")
  }

  ngOnInit() {
    console.log('sdfdsfsdfdsfs');
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  goBack() {
    this.router.navigate(['/login'])
  }
}
