import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable()
export class OtpStorage {

  public ritualSeasonSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  public constructor() {
    this.ritualSeasonSubject.asObservable();
  }

}
