import {Component, OnInit, ViewChild} from '@angular/core';
import {GoogleMap} from '@angular/google-maps';
import {Location} from "@angular/common";
import {CardService, UserService} from "@core/services";
import {TranslateService} from "@ngx-translate/core";
import {ApplicantRitualLite} from "@model/applicant-ritual-lite.model";
import {ToastService} from "@shared/components/toast";
import * as moment_ from 'moment-hijri';
import {LookupService} from "@core/utilities/lookup.service";
import {hijriMonth} from "@shared/helpers/hijri-month.helper";
import {CompanyRitualMainDataStep} from "@model/company-ritual-step";
import {I18nService} from "@dcc-commons-ng/services";
import {Lookup} from "@model/lookup.model";
import { MapOptions, Marker, Position } from '@app/_shared/model/marker.model';

const momentHijri = moment_;

@Component({
  selector: 'app-hajj-rituals',
  templateUrl: './hajj-rituals.component.html',
  styleUrls: ['./hajj-rituals.component.scss']
})
export class HajjRitualsComponent implements OnInit {
  markers: Marker[];
  addedAlert = false;
  MAP_ZOOM_OUT = 10;
  MAP_ZOOM_IN = 14;
  isMapZoomed = false;
  zoomedMarker = 0;
  ritualsSteps: CompanyRitualMainDataStep[] = [];
  transportationTypes: Lookup[] = [];
  ritualStepLabels = [];
  selectedMarker: Marker;
  selectedApplicantRitual: ApplicantRitualLite;
  clickCounter = 0;
  mapOptions: google.maps.MapOptions = {
    center: {lat: 21.423461874376475, lng: 39.825553299746616},
    zoom: this.MAP_ZOOM_OUT,
    disableDefaultUI: true
  }


  @ViewChild(GoogleMap) map!: GoogleMap;

  constructor(private location: Location,
              private cardService: CardService,
              private userService: UserService,
              private translate: TranslateService,
              private lookupsService: LookupService,
              private toastr: ToastService,
              private i18nService: I18nService) {
  }

  ngOnInit(): void {
    this.loadLookups();
    //set rituals steps
    this.userService.selectedApplicantRitual.subscribe(selectedApplicantRitual => {
      this.selectedApplicantRitual = selectedApplicantRitual;
      this.selectedApplicantRitual = JSON.parse(localStorage.getItem('selectedApplicantRitual'));
      this.findRitualSteps();
    });
  }

  ngAfterViewInit() {
    const bounds = this.getBounds(this.markers);
    this.map.googleMap.fitBounds(bounds);
  }

  getBounds(markers) {
    let north;
    let south;
    let east;
    let west;

    for (const marker of markers) {
      // set the coordinates to marker's lat and lng on the first run.
      // if the coordinates exist, get max or min depends on the coordinates.
      north = north !== undefined ? Math.max(north, marker.position.lat) : marker.position.lat;
      south = south !== undefined ? Math.min(south, marker.position.lat) : marker.position.lat;
      east = east !== undefined ? Math.max(east, marker.position.lng) : marker.position.lng;
      west = west !== undefined ? Math.min(west, marker.position.lng) : marker.position.lng;
    }
    ;

    const bounds = {north, south, east, west};

    return bounds;
  }

  zoomMap(stepId) {
    this.clickCounter+= 1;
    let bounds, north, south, east, west;
    this.isMapZoomed = (this.clickCounter % 2 == 0) ? true : false;
    if (this.isMapZoomed) {
      this.zoomedMarker = stepId;
      this.selectedMarker = this.markers.find(m => m.id == stepId);
      north = this.selectedMarker.position.lat;
      south = this.selectedMarker.position.lat;
      east = this.selectedMarker.position.lng;
      west = this.selectedMarker.position.lng;
      bounds = {north, south, east, west};
      this.map.googleMap.fitBounds(bounds);
      this.map.googleMap.setZoom(this.MAP_ZOOM_IN);

    } else {
      //this.zoomedMarker = 0;
      bounds = this.getBounds(this.markers);
      this.map.googleMap.setZoom(this.MAP_ZOOM_OUT);
      this.map.googleMap.fitBounds(bounds);
    }
  }

  goBack() {
    this.location.back();
  }

  findRitualSteps() {
    this.cardService.findTafweejDetails(this.selectedApplicantRitual?.id).subscribe(data => {
      if (data) {
        const today = new Date();
        this.ritualsSteps = data;
        this.ritualsSteps
          .forEach(step => {
            const stepTime = new Date(step.time);
            step.month = hijriMonth[momentHijri(step.time).iMonth()];
            step.day = momentHijri(step.time).iDate();
            step.isDone = new Date(step.time) < new Date();
            if (today.getUTCFullYear() === stepTime.getUTCFullYear() && today.getUTCMonth() === stepTime.getUTCMonth() && today.getUTCDate() === stepTime.getUTCDate()) {
              step.isActive = true;
              step.isDone = false;
            }
          });
          this.markers = this.ritualsSteps.map(step=>{
            let marker = new Marker(step.id, new Position(step.locationLat, step.locationLng), step.stepCode,
            new MapOptions('../../../assets/images/svg-icons/map-marker-light.svg'));
            return marker;
            })
      } else {
        this.toastr.error(this.translate.instant('general.route_item_not_found'),
          this.translate.instant('general.dialog_error_title'));
      }
    });
  }

  loadLookups() {
    this.cardService.findRitualStepsLabels().subscribe(result => {
      this.ritualStepLabels = result;
    });
    this.cardService.findTransportationType().subscribe(result => {
      this.transportationTypes = result;
    });
  }

  lookupService(): LookupService {
    return this.lookupsService;
  }

  get currentLanguage(): string {
    return this.i18nService.language;
  }
}
