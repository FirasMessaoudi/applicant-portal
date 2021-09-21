export class Position{
    lat: any;
    lng: any;
    constructor(lat: number, lng: number){
        this.lat = lat;
        this.lng = lng;
    }
};

export class MapOptions{
     icon: String;
     constructor(icon: String){
     //this.icon = '../../../assets/images/svg-icons/map-marker-light.svg'
     this.icon = icon;
     }
 };

export class Marker {
    id: number;
    position:Position;
    options: MapOptions;
    title: String

    constructor(id: number, positions: Position, title: String, options: MapOptions){
        this.id = id;
        this.position = positions;
        this.title = title;
        this.options = options;
    }
    }