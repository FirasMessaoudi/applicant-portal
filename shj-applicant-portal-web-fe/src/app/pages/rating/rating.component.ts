import {Component, OnInit} from '@angular/core';
import {SurveyService} from "@core/services/survey/survey.service";
import {LookupService} from "@core/utilities/lookup.service";
import {SurveyForm} from "@model/surver-form.model";
import {UserSurveyQuestion} from "@model/user-survey-question.model";
import {I18nService} from "@dcc-commons-ng/services";
import * as momentjs from "moment";
import {SurveyQuestion} from "@model/survey-question.model";
import {ToastService} from "@shared/components/toast";
import {TranslateService} from "@ngx-translate/core";
import {UserSurvey} from "@model/user-survey.model";
import * as moment_ from 'moment-hijri';

const moment = momentjs;
const momentHijri = moment_;
@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent implements OnInit {

  surveyForm: SurveyForm = new SurveyForm(new UserSurvey());
  userSurveyQuestions: Array<SurveyQuestion>;
  loading: boolean = false;
  surveyAlreadySubmitted : boolean = false;
  surveyNotAvailable : boolean = false;
  todayDate: any;
  surveyAvailable : boolean = false;

  constructor(private surveyService: SurveyService,
              private lookupsService: LookupService,
              private i18nService: I18nService,
              private toastr: ToastService, private translate: TranslateService) {
  }

  ngOnInit() {
    this.surveyNotAvailable = false;
    this.surveyAlreadySubmitted = false;
    this.loading = true;
    this.todayDate = momentHijri(new Date());
    this.surveyService.listSurveyQuestions("DAILY").subscribe(result => {

      if (result == 131) {
        this.surveyNotAvailable = true;
        this.surveyAlreadySubmitted = false;
        this.loading = false;
      } else if (result == 133) {
        this.surveyAlreadySubmitted = true;
        this.surveyNotAvailable = false;
        this.loading = false;
      } else {
        this.surveyNotAvailable = true;
        this.surveyAlreadySubmitted = true;
        this.surveyAvailable = true;
        this.loading = false;
        this.userSurveyQuestions = result.filter(sq => this.currentLanguage.startsWith(sq.lang)).map(sq => new SurveyQuestion(sq));
      }


    }, error => {
        this.loading = false;
        this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("survey.survey-error"));
    });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  onChangeRating(rate: any, question: SurveyQuestion) {
    question.rate = rate;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }

  submitSurvey() {
    const unratedQuestion =this.userSurveyQuestions.find(usq => usq.rate == 0);
    if(unratedQuestion){
      this.toastr.warning(this.translate.instant("survey.unrated_question_error"), this.translate.instant("survey.survey-error"));
      return;
    }
    this.loading = true;

    this.surveyForm.userSurvey.surveyType="DAILY";
    this.surveyForm.userSurvey.surveyDate = this.loadSurveyDate();

    let userSurveyQuestions: UserSurveyQuestion[] = this.userSurveyQuestions.map(usq => {
      return {'rate': usq.rate, 'surveyQuestion': usq.surveyQuestion.code, 'userSurvey': this.surveyForm.userSurvey}
    });

    this.surveyForm.userSurveyQuestions = userSurveyQuestions;
    this.surveyService.submitSurvey(this.surveyForm).subscribe(result =>{
      this.loading = false;

      if (result == 114) {
        this.toastr.warning(this.translate.instant("survey.user_not_found"), this.translate.instant("survey.survey-error"));
      } else if (result == 134) {
        this.toastr.warning(this.translate.instant("survey.invalid_survey_type"), this.translate.instant("survey.survey-error"));
      } else if (result == 131) {
        this.toastr.warning(this.translate.instant("survey.already_submitted"), this.translate.instant("survey.survey-error"));
      } else {
        this.toastr.success(this.translate.instant("survey.submit_successfully"), this.translate.instant("survey.survey_success_title"));
      }

    }, error => {
      console.log(error);
      this.loading = false;
      this.toastr.warning(this.translate.instant("general.dialog_form_error_text"), this.translate.instant("survey.survey-error"));
    });

  }

  loadSurveyDate() {
    let currentDate = moment(new Date());
    if (currentDate.hour() < 17)
      return currentDate.subtract(1, "days").toDate();
  else
    return new Date();
  }
  getTodayDate():string {
    return new Intl.DateTimeFormat(this.currentLanguage, { dateStyle: 'long' } as Intl.DateTimeFormatOptions).format(new Date())
  }
}
