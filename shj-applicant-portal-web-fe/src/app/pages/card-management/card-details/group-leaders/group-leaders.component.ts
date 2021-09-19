import {Component, Input} from "@angular/core";
import {GroupLeader} from "@model/group-leader.model";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";

@Component({
  selector: 'app-group-leaders',
  templateUrl: './group-leaders.component.html',
  styleUrls: ['./group-leaders.component.scss']
})
export class GroupLeadersComponent {

  @Input('applicantGroupLeaders')  groupLeaders: GroupLeader[]
  @Input('groupLeadersTitle') groupLeaderTitleLookups: Lookup[]
  lookupService: LookupService

  constructor(lookupsService: LookupService) {
    this.lookupService = lookupsService;
  }




}
