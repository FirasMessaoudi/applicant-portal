import {RoleListComponent} from './role-list/role-list.component';
import {RoleDetailsComponent} from "@pages/role-management/role-details/role-details.component";
import {RoleAddUpdateComponent} from "@pages/role-management/role-add-update/role-add-update.component";


export const roleManagement: any[] = [
  RoleListComponent,
  RoleDetailsComponent,
  RoleAddUpdateComponent
];


export * from "./role-list/role-list.component";
export * from "./role-details/role-details.component";
export * from "./role-add-update/role-add-update.component";
