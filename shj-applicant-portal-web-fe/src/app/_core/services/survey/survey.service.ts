import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SurveyQuestionLookup} from "@model/survey-question-lookup.model";
import {SurveyForm} from "@model/surver-form.model";

/**
 * Provides a base for survey operations.
 */
@Injectable()
export class SurveyService {

  constructor(private http: HttpClient) {
  }

  listSurveyQuestions(surveyType: string): Observable<any> {
    return this.http.get<any>('/core/api/survey/find-survey/' + surveyType);
  }

  submitSurvey(surveyForm: SurveyForm): Observable<any>{
    return this.http.post<any>("/core/api/survey/submit-survey", surveyForm);
  }
}
