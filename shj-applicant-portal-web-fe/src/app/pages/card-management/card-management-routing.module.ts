import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CardListComponent} from "@pages/card-management/card-list/card-list.component";
import {CardDetailsComponent} from "@pages/card-management/card-details/card-details.component";
import {MainDetailsComponent} from "@pages/card-management/card-details/main-details/main-details.component";
import {HajjCardComponent} from '@pages/card-management/hajj-card/hajj-card.component';

const routes: Routes = [
  //{path: 'cards/list', component: CardListComponent, canActivate: [AuthenticationGuard]},
  {path: 'cards/details/:id', component: CardDetailsComponent, canActivate: [AuthenticationGuard]},
  {path: 'cards/hajj-card/:id', component: HajjCardComponent, canActivate: [AuthenticationGuard]}
  //{path: 'cards/details/main/:id', component: MainDetailsComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CardManagementRoutingModule { }
