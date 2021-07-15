import {NavigationEnd, Router} from "@angular/router";
import {Location} from '@angular/common'
import {Injectable} from "@angular/core";


@Injectable({providedIn: 'root'})
export class NavigationService {
  private history: string[] = []
  goBackURL: string;
  constructor(private router: Router, private location: Location) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.history.push(event.urlAfterRedirects)
      }
    })
  }

  public getHistory(): string[] {
    return this.history;
  }

  public getPreviousUrl(): string {
    return this.history[this.history.length - 2] || '/';
  }


  back(url?: string) {
    this.history.pop();
    console.log(this.goBackURL);
    if (url == '' || url == undefined || this.history.length > 0) {
      this.location.back();
    } else {
      this.router.navigateByUrl(url);
    }
  }

}
