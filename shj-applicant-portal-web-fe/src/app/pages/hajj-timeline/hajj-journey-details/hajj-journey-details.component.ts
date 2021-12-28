import { Component, OnInit, Input } from '@angular/core';
import {Location} from "@angular/common";
import {RitualTimelineService} from "@core/services/timeline/ritual-timeline.service";
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  selector: 'app-hajj-journey-details',
  templateUrl: './hajj-journey-details.component.html',
  styleUrls: ['./hajj-journey-details.component.scss']
})
export class HajjJourneyDetailsComponent implements OnInit {

  ritualStepDescription:string

  constructor(  private location: Location, private ritualTimelineService: RitualTimelineService,private route: ActivatedRoute,
  private router: Router) { }

  ngOnInit(): void {
    this.ritualTimelineService.ritualStepDescriptionSubject?.subscribe(data=>{
      this.ritualStepDescription=data;
    })
  }
  goBack() {
    this.router.navigate(['/hajj-journey/list'], {replaceUrl: true});
  }

}
