import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {RoleListComponent} from './role-list/role-list.component';
import {RoleDetailsComponent} from "@pages/role-management/role-details/role-details.component";
import {RoleAddUpdateComponent} from "@pages/role-management/role-add-update/role-add-update.component";
import {AuthenticationGuard} from "@core/services";

const routes: Routes = [
  {path: 'roles/create', component: RoleAddUpdateComponent, canActivate: [AuthenticationGuard]},
  {path: 'roles/update/:id', component: RoleAddUpdateComponent, canActivate: [AuthenticationGuard]},
  {path: 'roles/list', component: RoleListComponent, canActivate: [AuthenticationGuard]},
  {path: 'roles/details/:id', component: RoleDetailsComponent, canActivate: [AuthenticationGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class RoleManagementRoutingModule {
}
