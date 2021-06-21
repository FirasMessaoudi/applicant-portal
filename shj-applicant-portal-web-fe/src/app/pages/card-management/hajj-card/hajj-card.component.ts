import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";

@Component({
  selector: 'app-hajj-card',
  templateUrl: './hajj-card.component.html',
  styleUrls: ['./hajj-card.component.scss']
})
export class HajjCardComponent implements OnInit {

  constructor( private location: Location) { }

  ngOnInit(): void {
  }

  goBack() {
    this.location.back();
  }

}
