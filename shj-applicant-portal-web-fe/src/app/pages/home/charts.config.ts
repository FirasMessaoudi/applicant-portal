import {Color} from "ng2-charts";
import {Chart, ChartType} from "chart.js";
import ChartDataLabels from 'chartjs-plugin-datalabels';

const ACTIVE_LABEL: string = 'نشط';
const INACTIVE_LABEL: string = 'غير نشط';
const DELETED_LABEL: string = 'محذوف';

const TOTAL_USERS_LABEL: string = 'إجمالي المستخدمين';

const FONTS: string = '"Elm-font", sans-serif';

export class ChartsConfig {

  constructor() {
    if (Chart.defaults.global.plugins.datalabels) {
      Chart.defaults.global.plugins.datalabels.color = 'white';
      Chart.defaults.global.plugins.datalabels.textAlign = 'center';
    }
    Chart.defaults.global.plugins = [ChartDataLabels];
    Chart.defaults.global.defaultFontFamily = 'Elm-Font';

  }

  // barChart
  barChartData: Array<any> = [
    {
      data: new Array(24).fill(0),
      label: 'جديد', stack: 'a'
    },
    {
      data: new Array(24).fill(0),
      label: ACTIVE_LABEL, stack: 'a'
    },
    {
      data: new Array(24).fill(0),
      label: INACTIVE_LABEL, stack: 'a'
    },
    {
      data: new Array(24).fill(0),
      label: DELETED_LABEL, stack: 'a'
    }
  ];

  barChartLabels: Array<any>;
  barChartOptions: any = {
    responsive: true,
    maintainAspectRatio: true,
    elements: {
      line: {
        fill: false
      },
      point: {
        hoverRadius: 7,
        radius: 5
      }
    },
    plugins: // [ChartDataLabels],
      {
        datalabels: {
          color: 'white',
          display: function (context: any) {
            return context.dataset.data[context.dataIndex] > 15;
          },
          textAlign: 'center',
          font: {
            weight: 'bold'
          },
          formatter: Math.round
        }
      },
    legend: {
      labels: {
        fontFamily: FONTS,
        fontSize: 12,
        padding: 6,
        fontColor: '#777',
        boxWidth: 10,
      },
      position: 'bottom'
    },
    scales: {
      xAxes: [{
        stacked: true
      }],
      yAxes: [{
        stacked: true,
        ticks: {
          beginAtZero: true
        }
      }]
    }
  };
  pieChartColors: Color[] = [
    {
      backgroundColor: ['#289E9A', '#FE9B43', '#AD323B', '#767676', '#FF4B12', '#7ABE6C', '#67C7E0', '#5E56A2'],
      hoverBackgroundColor: ['#47B5B1', '#FFAE66', '#CE5962', '#B0B0B0', '#FB9373', '#B6EAAC', '#B3E4F1', '#B5AEEF']
    }
  ];
  polarChartColors: Color[] = [
    {
      backgroundColor: ['#5E56A2', '#67C7E0', '#7ABE6C', '#767676', '#FF4B12', '#289E9A', '#FE9B43', '#AD323B'],
      hoverBackgroundColor: ['#B5AEEF', '#B3E4F1', '#B6EAAC', '#B0B0B0', '#FB9373', '#47B5B1', '#FFAE66', '#CE5962']
    }
  ];
  barChartColors: Color[] = [
    {backgroundColor: '#289E9A', hoverBackgroundColor: '#47B5B1'},
    {backgroundColor: '#7ABE6C', hoverBackgroundColor: '#B6EAAC'},
    {backgroundColor: '#FE9B43', hoverBackgroundColor: '#FFAE66'},
    {backgroundColor: '#AD323B', hoverBackgroundColor: '#CE5962'},
    {backgroundColor: '#767676', hoverBackgroundColor: '#B0B0B0'},
    {backgroundColor: '#FF4B12', hoverBackgroundColor: '#FB9373'},
    {backgroundColor: '#67C7E0', hoverBackgroundColor: '#B3E4F1'},
    {backgroundColor: '#5E56A2', hoverBackgroundColor: '#B5AEEF'}
  ];

  // doughnut
  doughnutChartLabels: string[] = [ACTIVE_LABEL, INACTIVE_LABEL, DELETED_LABEL];
  doughnutChartOptions: any = {
    circumference: Math.PI,
    rotation: 1.0 * Math.PI,
    cutoutPercentage: 95,
    responsive: true,
    maintainAspectRatio: false,
    aspectRatio: 4 / 3,
    layout: {
      padding: {
        left: 12,
        right: 12,
        top: 12,
        bottom: 0
      }
    },
    elements: {
      line: {
        fill: false
      },
      point: {
        hoverRadius: 7,
        radius: 5
      }
    },
    plugins: {
      datalabels: {
        backgroundColor: function (context: any) {
          return context.dataset.backgroundColor;
        },
        borderColor: 'white',
        borderRadius: 45,
        borderWidth: 2,
        offset: 10,
        padding: 8,
        anchor: 'end',
        textAlign: 'center',
        color: 'white',
        display: function (context: any) {
          const dataset = context.dataset;
          const count = dataset.data.length;
          const value = dataset.data[context.dataIndex];
          return value > count * 1.5;
        },
        font: {
          weight: 'bold'
        },
        formatter: Math.round
      }
    },
    legend: {
      labels: {
        fontFamily: FONTS,
        fontSize: 11,
        padding: 6,
        fontColor: '#777',
        boxWidth: 10,
      },
      position: 'bottom'
    },
    animation: {
      onProgress: function (chart: any) {
        drawText(chart.chart, 4.5, TOTAL_USERS_LABEL);
      },
      onComplete: function (chart: any) {
        drawText(chart.chart, 4.5, TOTAL_USERS_LABEL);
      },
    }
  };

  // PolarArea
  polarAreaChartLabels: any[] = [];
  polarAreaChartData: any[] = [];
  polarAreaLegend = true;

  polarAreaChartType: ChartType = 'polarArea';

  polarAreaChartOptions: any = {
    responsive: true,
    maintainAspectRatio: false,
    layout: {
      padding: 8
    },
    elements: {
      line: {
        fill: false
      },
      point: {
        hoverRadius: 7,
        radius: 5
      }
    },
    plugins: {},
    legend: {
      labels: {
        fontFamily: FONTS,
        fontSize: 12,
        padding: 8,
        fontColor: '#777',
        boxWidth: 12,
      },
      position: 'bottom'
    }
  };

}

const drawText = function (chart: any, fontSize: number, title: string) {
  const width = chart.width, height = chart.height - 50, ctx = chart.ctx;
  let total = 0;
  chart.data.datasets[0].data.forEach((v: any) => total += v);
  ctx.restore();
  ctx.font = fontSize + 'em ' + FONTS;
  ctx.textBaseline = 'middle';
  ctx.fillStyle = '#952d98';
  const text = '' + total, legendHeight = chart.chart.legend.height,
    textY = height / 2.3 + legendHeight;
  let textX = Math.round((width - ctx.measureText(text).width) / 2);
  ctx.fillText(text, textX, textY);
  ctx.font = (+fontSize / 4.5) + 'em ' + FONTS;
  ctx.textBaseline = 'middle';
  ctx.fillStyle = '#777';
  textX = Math.round((width - ctx.measureText(title).width) / 2)
  ctx.fillText(title, textX, textY + (10 * +fontSize));
  ctx.save();
};

