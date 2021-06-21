export class Role {
  id: number;
  nameArabic: string;
  nameEnglish: string;
  deleted: boolean;
  activated: boolean;
  roleAuthorities: Array<any>;
  creationDate: Date;
  updateDate: Date;
}
