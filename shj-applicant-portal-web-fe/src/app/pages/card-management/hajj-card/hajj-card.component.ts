import {Component, OnInit} from '@angular/core';
import {Location} from "@angular/common";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-hajj-card',
  templateUrl: './hajj-card.component.html',
  styleUrls: ['./hajj-card.component.scss']
})
export class HajjCardComponent implements OnInit {
  uin = '';
  cardStatus = '';

  constructor(private location: Location, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
        this.uin = params['uin'];
        this.cardStatus = params['cardStatus'];
      }
    )
  }

  goBack() {
    this.location.back();
  }

}
