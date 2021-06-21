import {Role} from "@model/role.model";
import {Authority} from "@model/authority.model";

export class RoleAuthority {
  id: number;
  role: Role;
  authority: Authority;
  creationDate: Date;
  // transient attribute used only for display
  selected: boolean;
}
