import {UserRole} from "@model/user-role.model";

export class User {
  id: number;
  dateOfBirthGregorian: any;
  dateOfBirthHijri: any;
  email: string;
  familyName: string;
  firstName: string;
  gender: string;
  grandFatherName: string;
  fatherName: string;
  lastLoginDate: Date;
  mobileNumber: any;
  nin: number;
  avatarFile: any;
  avatar: string;
  userName: string;
  password: string;
  activated: any;
  blocked: boolean;
  changePasswordRequired: boolean;
  creationDate: Date;
  authorities: any;
  userRoles: Array<UserRole>;
}
