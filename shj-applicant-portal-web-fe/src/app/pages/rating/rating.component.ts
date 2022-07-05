import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent implements OnInit {

  currentRate: number[] = [3, 4, 1, 2, 4, 2, 1];
  
  constructor() { }

  ngOnInit() {
  }

}
