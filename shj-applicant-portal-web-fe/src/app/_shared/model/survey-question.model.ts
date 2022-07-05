import {SurveyQuestionLookup} from "@model/survey-question-lookup.model";

export class SurveyQuestion {
  rate: number;
  surveyQuestion: SurveyQuestionLookup;

  constructor(surveyQuestion: SurveyQuestionLookup) {
    this.surveyQuestion = surveyQuestion;
    this.rate = 0;
  }
}

