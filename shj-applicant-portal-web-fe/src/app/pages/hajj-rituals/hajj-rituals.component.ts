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

const momentHijri = moment_;

@Component({
  selector: 'app-hajj-rituals',
  templateUrl: './hajj-rituals.component.html',
  styleUrls: ['./hajj-rituals.component.scss']
})
export class HajjRitualsComponent implements OnInit {
  addedAlert = false;
  MAP_ZOOM_OUT = 10;
  MAP_ZOOM_IN = 14;
  isMapZoomed = false;
  zoomedMarker = 0;
  ritualsSteps: CompanyRitualMainDataStep[] = [];
  transportationTypes: Lookup[] = [];
  ritualStepLabels = [];
  selectedApplicantRitual: ApplicantRitualLite;
  mapOptions: google.maps.MapOptions = {
    center: {lat: 21.423461874376475, lng: 39.825553299746616},
    zoom: this.MAP_ZOOM_OUT,
    disableDefaultUI: true
  }

  // Set map markers
  markers = [
    {
      position: {lat: 21.423461874376475, lng: 39.825553299746616},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '1'
    },
    {
      position: {lat: 21.40190096469824, lng: 39.89745989622648},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '2'
    },
    {
      position: {lat: 21.394148460136233, lng: 39.90002384324027},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '3'
    },
    {
      position: {lat: 21.423461874376475, lng: 39.825553299746616},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '4'
    },
    {
      position: {lat: 21.40190096469824, lng: 39.89745989622648},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '5'
    },
    {
      position: {lat: 21.394148460136233, lng: 39.90002384324027},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '6'
    },
    {
      position: {lat: 21.423461874376475, lng: 39.825553299746616},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '7'
    },
    {
      position: {lat: 21.40190096469824, lng: 39.89745989622648},
      options: {
        icon: '../../../assets/images/svg-icons/map-marker-light.svg'
      },
      title: '8'
    },

  ];
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
    let bounds, north, south, east, west;
    this.isMapZoomed = !this.isMapZoomed;

    if (this.isMapZoomed) {
      this.zoomedMarker = stepId;
      north = this.markers[this.zoomedMarker - 1].position.lat;
      south = this.markers[this.zoomedMarker - 1].position.lat;
      east = this.markers[this.zoomedMarker - 1].position.lng;
      west = this.markers[this.zoomedMarker - 1].position.lng;
      bounds = {north, south, east, west};
      this.map.googleMap.fitBounds(bounds);
      this.map.googleMap.setZoom(this.MAP_ZOOM_IN);

    } else {
      this.zoomedMarker = 0;
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
