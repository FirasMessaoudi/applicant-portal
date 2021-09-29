import {Lookup} from "@model/lookup.model";

export class RitualSeason {
  id: number;
  seasonYear: number;
  ritualTypeCode: string;
  seasonStart: number;
  seasonEnd: number;
  activated: boolean;
}
