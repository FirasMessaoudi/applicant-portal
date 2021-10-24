export class DetailedUserNotification {
  id: number;
  resolvedBody: string;
  statusCode: string;
  categoryCode: string;
  important: boolean;
  actionRequired: boolean;
  userSpecific: boolean;
  title: string;
  actionLabel: string;
  creationDate: Date;
}
