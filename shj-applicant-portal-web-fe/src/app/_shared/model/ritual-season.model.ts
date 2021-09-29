import {Lookup} from "@model/lookup.model";

export class RitualSeason {
  id: number;
  seasonYear: number;
  ritualTypeCode: Lookup;
  seasonStart: number;
  seasonEnd: number;
  activated: boolean;
}
