import {Component, OnDestroy, OnInit, ViewEncapsulation} from "@angular/core";
import {RuleService} from '@core/services/rule/rule.service';
import {Page} from "@shared/model";
import {Subscription} from "rxjs";
import {I18nService} from "@dcc-commons-ng/services";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {DecisionRule} from '@model/decision-rule.model';

@Component({
  selector: "app-rule-list",
  encapsulation: ViewEncapsulation.None,
  templateUrl: "./rule-list.component.html",
  styleUrls: ["./rule-list.component.scss"],
})
export class RuleListComponent implements OnInit, OnDestroy {
  rules: Array<DecisionRule>;
  pageArray: Array<number>;
  page: Page;
  private listSubscription: Subscription;

  constructor(private i18nService: I18nService,
              private ruleService: RuleService,
              private toastr: ToastService,
              private translate: TranslateService,) {
  }

  ngOnInit() {
    this.loadPage(0);
  }

  ngOnDestroy() {
    if (this.listSubscription) {
      this.listSubscription.unsubscribe();
    }
  }

  loadPage(page: number) {
    // load roles for param page
    this.listSubscription = this.ruleService.list(page).subscribe(data => {
      this.page = data;
      if (this.page != null) {
        this.pageArray = Array.from(this.pageCounter(this.page.totalPages));
        this.rules = this.page.content;
      }
    });
  }

  pageCounter(i: number): Array<number> {
    return new Array(i);
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

}
