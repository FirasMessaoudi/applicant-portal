import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from './user-list/user-list.component';
import {UserDetailsComponent} from "./user-details/user-details.component";
import {UserAddUpdateComponent} from "./user-add-update/user-add-update.component";
import {AuthenticationGuard} from "@core/services";

const routes: Routes = [
  {path: 'users/create', component: UserAddUpdateComponent, canActivate: [AuthenticationGuard]},
  {path: 'users/update/:id', component: UserAddUpdateComponent, canActivate: [AuthenticationGuard]},
  {path: 'users/list', component: UserListComponent, canActivate: [AuthenticationGuard]},
  {path: 'users/details/:id', component: UserDetailsComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class UserManagementRoutingModule {
}
