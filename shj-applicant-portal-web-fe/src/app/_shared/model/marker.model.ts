export class Position {
  lat: any;
  lng: any;

  constructor(lat: number, lng: number) {
    this.lat = lat;
    this.lng = lng;
  }
}

export class MapOptions {
  icon: any;

  constructor(iconURL: String) {
    //this.icon = '../../../assets/images/svg-icons/map-marker-light.svg'
    this.icon = {
      url: iconURL, // url
      scaledSize: new google.maps.Size(50, 50), // scaled size
    };
  }
}

export class Marker {
  id: number;
  position: Position;
  options: MapOptions;
  title: String

  constructor(id: number, positions: Position, title: String, options: MapOptions) {
    this.id = id;
    this.position = positions;
    this.title = title;
    this.options = options;
  }
}
