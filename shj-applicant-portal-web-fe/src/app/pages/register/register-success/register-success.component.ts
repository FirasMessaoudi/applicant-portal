import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {I18nService} from "@dcc-commons-ng/services";

@Component({
  selector: 'app-register-success',
  templateUrl: './register-success.component.html',
  styleUrls: ['./register-success.component.scss']
})
export class RegisterSuccessComponent implements OnInit {


  constructor(private router: Router,
              private i18nService: I18nService,) {
  }

  ngOnInit() {
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  goBack() {
    this.router.navigate(['/login']);
  }
}
