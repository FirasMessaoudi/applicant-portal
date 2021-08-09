import {UserRole} from "@model/user-role.model";

export class User {
  id: number;
  dateOfBirthGregorian: any;
  dateOfBirthHijri: any;
  email: string;
  fullNameEn: string;
  fullNameAr: string;
  gender: string;
  grandFatherName: string;
  lastLoginDate: Date;
  mobileNumber: any;
  nin: number;
  uin: number;
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
  otpExpiryMinutes: number;
  maskedMobileNumber: string;
  countryCode: string;
  countryPhonePrefix: string;
}
