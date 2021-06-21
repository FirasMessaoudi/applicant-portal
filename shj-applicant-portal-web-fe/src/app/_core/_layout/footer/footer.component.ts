import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-footer',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  currentYear: number = new Date(Date.now()).getFullYear();

  @Input()
  backgroundClass: string = 'swatch-dcc-primary';

  @Input()
  textClass: string = 'text-white-50';

  @Input()
  linkClass: string = 'text-white';

  constructor() {}

  ngOnInit() {}
}
