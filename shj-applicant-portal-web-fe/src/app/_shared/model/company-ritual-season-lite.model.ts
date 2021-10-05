import {RitualSeason} from "@model/ritual-season.model";

export class CompanyRitualSeasonLite {
  id: number;
  seasonStart: number;
  seasonEnd: number;
  ritualSeason: RitualSeason;
  selected: boolean;
}
