import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-hajj-journey',
  templateUrl: './hajj-journey.component.html',
  styleUrls: ['./hajj-journey.component.scss']
})
export class HajjJourneyComponent implements OnInit {

  activitesList = [];

  constructor() { }

  ngOnInit(): void {
    this.activitesList = [
      {
        id: 1,
        date: "7 ذى الحجة",
        row: [{
          description: "يمتدُّ وقته إلى حين الوقوف بعرفة",
          activites: ["طواف القدوم"],
        }],
        isActive: false
      },
      {
        id: 2,
        date: "8 ذى الحجة",
        row: [{
          description: "",
          activites: ["الاحرام والمبيت في منى"],
        }],
        isActive: false
      },
      {
        id: 3,
        date: "9 ذى الحجة",
        row: [{
          description: "من الزوال حتى ما قَبل الفجر",
          activites: ["الوقوف في عرفة"]
        },
        {
          description: "بعد مغيب الشمس",
          activites: ["المبيت في مزدلفة"]
        }

        ],
        isActive: true
      },
      {
        id: 4,
        date: "10 ذى الحجة",
        row: [
          {
            description: "قبل طُلوع الشمس",
            activites: ["الحلق والتقصير",
              "رمي جمرة العقبة",
              "ذبح الهدي"],
          },
          {
            description: "بعد مغيب الشمس",
            activites: ["طواف الإفاضة",
              "التحلل من الاحرام"],
          }],
        isActive: false
      },
      {
        id: 5,
        date: "11 - 12 ذى الحجة",
        row: [{
          description: "التعجُّل قبل مغيب شمس 12",
          activites: ["رمي الجمار الثلاث"],
        }],
        isActive: false
      },
      {
        id: 6,
        date: "13 ذى الحجة",
        row: [{
          description: "في حال عدم التعجل",
          activites: ["طواف الوداع"],
        }],
        isActive: false
      }
    ];
  }

}
