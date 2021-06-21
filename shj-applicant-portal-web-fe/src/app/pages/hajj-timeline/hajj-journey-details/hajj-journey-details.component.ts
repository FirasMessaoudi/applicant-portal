import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";

@Component({
  selector: 'app-hajj-journey-details',
  templateUrl: './hajj-journey-details.component.html',
  styleUrls: ['./hajj-journey-details.component.scss']
})
export class HajjJourneyDetailsComponent implements OnInit {

  constructor(  private location: Location,) { }

  ngOnInit(): void {
  }
  goBack() {
    this.location.back();
  }

}
