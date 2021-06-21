import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService, DashboardService} from '@app/_core/services';
import {EAuthority} from "@model/enum/authority.enum";
import {DashboardVo} from "@model/dashboard-vo.model";
import {ChartsConfig} from "@pages/home/charts.config";

export enum PeriodType {
  DAILY = 1,
  WEEKLY = 2,
  MONTHLY = 3
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  PeriodType = PeriodType;

  periodType: PeriodType = PeriodType.DAILY;
  monthDays: Array<any>;
  weekDays: Array<any>;
  dayHours: Array<any>;

  data: DashboardVo;

  chartsConfig: ChartsConfig = new ChartsConfig();
  private currentMonthNumberOfDays: number;

  constructor(
    private router: Router,
    private dashboardService: DashboardService,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit() {
    this.dashboardService.loadData().subscribe(data => {
      this.data = data;
      this.chartsConfig.polarAreaChartLabels = new Array(data.usersByParentAuthorityCountVoList.length).fill('')
        .map((v: any, i: number) => data.usersByParentAuthorityCountVoList[i].label);
      this.chartsConfig.polarAreaChartData = new Array(data.usersByParentAuthorityCountVoList.length).fill('')
        .map((v: any, i: number) => data.usersByParentAuthorityCountVoList[i].count);
    });

    this.loadByPeriod(PeriodType.DAILY);

    this.monthDays = [];
    const now = new Date(), currentMonth = now.getMonth();
    this.currentMonthNumberOfDays = new Date(now.getFullYear(), now.getMonth(), 0).getDate();
    this.monthDays = (new Array(31)).fill('').map((v, i) => new Date(now.getFullYear(), currentMonth, i + 1)).filter(v => v.getMonth() === currentMonth).map(v => v.getDate());
    this.weekDays = ['اﻷحد', 'اﻷثنين', 'الثلاثاء', 'اﻷربعاء', 'الخميس', 'الجمعة', 'السبت'];
    this.dayHours = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'];
    this.chartsConfig.barChartLabels = [...this.dayHours];
  }

  downloadChartImage(event: any, chartEl: any) {
    const anchor = event.target;
    // get the png image
    anchor.href = chartEl.toDataURL();
  }

  loadByPeriod(periodType: PeriodType) {
    this.periodType = periodType;

    switch (+this.periodType) {
      case PeriodType.DAILY:
        console.log('DAILY.......');

        this.dashboardService.loadPeriodData('D').subscribe((data: DashboardVo) => {
          console.log(data);
          this.updateBarChartData(24, data);
          this.chartsConfig.barChartLabels = [...this.dayHours];
        }, error => {
          this.updateBarChartData(24, null);
          this.chartsConfig.barChartLabels = [...this.dayHours];
        });

        break;
      case PeriodType.WEEKLY:
        console.log('WEEKLY.......');

        this.dashboardService.loadPeriodData('W').subscribe((data: DashboardVo) => {
          console.log(data);
          this.updateBarChartData(7, data);
          this.chartsConfig.barChartLabels = [...this.weekDays];
        }, error => {
          this.updateBarChartData(7, null);
          this.chartsConfig.barChartLabels = [...this.weekDays];
        });

        break;
      case PeriodType.MONTHLY:
        console.log('MONTHLY.......');

        this.dashboardService.loadPeriodData('M').subscribe((data: DashboardVo) => {
          console.log(data);
          this.updateBarChartData(this.currentMonthNumberOfDays, data);
          this.chartsConfig.barChartLabels = [...this.monthDays];
        }, error => {
          this.updateBarChartData(this.currentMonthNumberOfDays, null);
          this.chartsConfig.barChartLabels = [...this.monthDays];
        });

        break;
      default:
        break;
    }

  }

  private updateBarChartData(periodItemsCount: number, data: any) {
    this.chartsConfig.barChartData = [
      {
        data: this.fillPeriodStatisticsData(periodItemsCount, data, 'createdUsersCountVoList'),
        label: 'جديد', stack: 'a'
      },
      {
        data: this.fillPeriodStatisticsData(periodItemsCount, data, 'activeUsersCountVoList'),
        label: 'نشط', stack: 'a'
      },
      {
        data: this.fillPeriodStatisticsData(periodItemsCount, data, 'inactiveUsersCountVoList'),
        label: 'غير نشط', stack: 'a'
      },
      {
        data: this.fillPeriodStatisticsData(periodItemsCount, data, 'deletedUsersCountVoList'),
        label: 'محذوف', stack: 'a'
      }
    ];
  }

  private fillPeriodStatisticsData(periodItemsCount: number, data: any, arrayName: string): Array<any> {
    const adjustIndex: number = (periodItemsCount === 24) ? 0 : 1;
    const targetArray: Array<any> = Array.from(Array(periodItemsCount).keys());
    if (data && !data[arrayName]) {
      data[arrayName] = Array.from(Array(periodItemsCount).keys()).fill(0);
    }
    if (data && data[arrayName]) {
      targetArray.forEach((vi: number, i: number) => {
        targetArray[i] = 0;
        data[arrayName].forEach((vj: any) => {
          if (vi === (+vj.labelNumber - adjustIndex)) {
            targetArray[i] = vj.count;
          }
        });
      });
    }
    return targetArray;
  }

  get canSeeDashboard(): boolean {
    return this.authenticationService.hasAuthority(EAuthority.ADMIN_DASHBOARD);
  }
}


