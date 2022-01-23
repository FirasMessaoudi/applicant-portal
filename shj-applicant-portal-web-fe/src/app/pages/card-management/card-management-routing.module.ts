import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthenticationGuard} from "@core/guards/authentication.guard";
import {CardDetailsComponent} from "@pages/card-management/card-details/card-details.component";
import {HajjCardComponent} from '@pages/card-management/hajj-card/hajj-card.component';

const routes: Routes = [
  {path: 'cards/details/:id', component: CardDetailsComponent, canActivate: [AuthenticationGuard]},
  {path: 'cards/hajj-card/:uin/:cardStatus', component: HajjCardComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CardManagementRoutingModule { }
