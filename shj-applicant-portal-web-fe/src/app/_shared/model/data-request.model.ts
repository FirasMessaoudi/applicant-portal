import {DataSegment} from "@model/data-segment.model";

export class DataRequest {
  id: number;
  referenceNumber: string;
  originalSourcePath: string;
  errorFilePath: string;
  channel: string;
  dataSegment: DataSegment;
  status: any;
  creationDate: Date;
  updateDate: Date;
  itemCount: number;
}
