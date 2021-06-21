export class UserStatus {

  constructor(id: number, nameAr:string, nameEn:string, activated: boolean) {
    this.id = id;
    this.nameArabic = nameAr;
    this.nameEnglish = nameEn;
    this.activated = activated;
  }

  id: number;
  nameArabic: string;
  nameEnglish: string;
  activated: boolean;
}
