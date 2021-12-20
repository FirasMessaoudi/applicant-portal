export class UserNotificationCategoryPreference {
    id: number;
    userId: string;
    categoryCode: string;
    enabled: boolean;
    creationDate: string;
    constructor(categoryCode: string, enabled: boolean){
      this.categoryCode = categoryCode;
      this.enabled = enabled;
    }
  }