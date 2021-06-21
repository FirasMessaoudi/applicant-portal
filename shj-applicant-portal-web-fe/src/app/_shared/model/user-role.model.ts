import {Role} from "@model/role.model";
import {User} from "@model/user.model";

export class UserRole {
  id: number;
  user: User;
  role: Role;
  mainRole: boolean;
  creationDate: Date;
}
