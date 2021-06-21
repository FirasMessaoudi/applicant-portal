import {UserDetailsComponent} from './user-details/user-details.component';
import {UserListComponent} from './user-list/user-list.component';
import {UserAddUpdateComponent} from "./user-add-update/user-add-update.component";

export const userManagement: any[] = [
  UserAddUpdateComponent,
  UserDetailsComponent,
  UserListComponent
];

export * from "./user-add-update/user-add-update.component";
export * from "./user-details/user-details.component";
export * from "./user-list/user-list.component";
