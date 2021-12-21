export class NotificationCategory {
    id: number;
    code: string;
    lang: string;
    label: string;
    sample: string;
    mandatory: boolean;
    enabled: boolean;
    creationDate: string;

    constructor(id: number, code: string, sample: string, mandatory: boolean, enabled:boolean){
      this.id = id;
      this.code = code;
      this.sample = sample;
      this.mandatory = mandatory;
      this.enabled = enabled;
    }
  }