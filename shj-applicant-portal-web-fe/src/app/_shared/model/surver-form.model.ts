import {UserSurvey} from "@model/user-survey.model";
import {UserSurveyQuestion} from "@model/user-survey-question.model";

export class SurveyForm {
  userSurvey: UserSurvey;
  userSurveyQuestions: UserSurveyQuestion[];
  constructor(userSurvey: UserSurvey) {
    this.userSurvey = userSurvey;
  }
}

