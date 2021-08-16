import { Component, OnInit } from '@angular/core';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {CardService, UserService} from "@core/services";
import {Lookup} from "@model/lookup.model";
import {LookupService} from "@core/utilities/lookup.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  closeResult = '';
  seasons: number [] = [];
  applicantRituals: ApplicantRitualLite [] = [];
  selectedSeason: number;
  selectedApplicantRitual: ApplicantRitualLite;
  ritualTypes: Lookup[] =[];
  wantEdit=false;

  constructor(private modalService: NgbModal,
              private userService: UserService,
              private cardService: CardService,
              private lookupsService: LookupService) { }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnInit() {

    this.cardService.findRitualTypes().subscribe(result => {
      this.ritualTypes = result;
    });

    this.selectedSeason =JSON.parse((localStorage.getItem('selectedSeason')));
    this.selectedApplicantRitual =JSON.parse(localStorage.getItem('selectedApplicantRitual'));

    this.getRitualSeason();
  }

  getRitualSeason() {
    this.userService.getApplicantSeason().subscribe(seasons => {
      console.log('seasons', seasons);
      this.seasons = seasons;
      if (this.seasons.length > 0) {
        console.log("this.selectedSeasonvv",this.selectedSeason)
        if(this.selectedSeason==null){
          this.selectedSeason = this.seasons[0];
        }

        localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
        this.getApplicantRitualLiteBySeason(true);
      }
    })
  }

  getApplicantRitualLiteBySeason(firstCall: boolean) {

    if (this.selectedSeason) {
      this.applicantRituals = [];
      this.userService.getApplicantRitualLiteBySeason(this.selectedSeason).subscribe(applicantRituals => {
        this.applicantRituals = applicantRituals;
        if (this.applicantRituals.length > 0) {

          if (firstCall==true && this.selectedApplicantRitual!=null) {
            let curSelected = this.applicantRituals.filter(ar => ar.id === this.selectedApplicantRitual.id);
            if (curSelected.length>0) {
              this.changeSelectedApplicantRitual(this.selectedApplicantRitual);
            }else{
              this.changeSelectedApplicantRitual(this.applicantRituals[0]);
            }
          }else {
            this.changeSelectedApplicantRitual(this.applicantRituals[0]);
          }
        }
      });
    }
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }
  onSeasonChange(selectedSeason: number) {
    this.selectedSeason = selectedSeason;
    this.getApplicantRitualLiteBySeason(false);
  }

  changeSelectedApplicantRitual(applicantRitual: ApplicantRitualLite) {
    this.selectedApplicantRitual = applicantRitual;

  }

  onApplicantRitualChange(applicantRitualId: number){
    let curSelected = this.applicantRituals.filter(ar => ar.id == applicantRitualId)[0];
    this.changeSelectedApplicantRitual(curSelected);
  }

  saveSelectedRitual() {
    localStorage.setItem('selectedSeason', JSON.stringify(this.selectedSeason));
    localStorage.setItem('selectedApplicantRitual', JSON.stringify(this.selectedApplicantRitual));
    this.userService.changeSelectedApplicantRitual(this.selectedApplicantRitual);

    this.wantEdit=false;
  }

  editRitual() {
    this.wantEdit=true;
  }
}
