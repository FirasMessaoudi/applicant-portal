import { trigger, animate, style, transition, animateChild, keyframes, group, query, stagger, state } from '@angular/animations';

export const $animations = [

  trigger('slideItemListAnimation', [
    transition('* => *', [
      query(':enter', style({ transform: 'translateY(100%)', opacity: 0 }), { optional: true }),
      query(':enter', stagger('300ms',
        animate('1s cubic-bezier(.8, -0.6, 0.2, 1.5)', keyframes([
          style({ transform: 'translateY(100%)', opacity: 0 }),
          style({ transform: 'translateY(0)', opacity: 1 })
        ]))), { optional: true }),
      query(':leave', stagger('100ms', [
        animate('1s cubic-bezier(.8, -0.6, 0.2, 1.5)', keyframes([
          style({ opacity: 1, transform: 'translateY(0)', offset: 0 }),
          style({ opacity: 0, transform: 'scale(1.1)', offset: 1 }),
        ]))]), { optional: true })
    ]),
  ]),

  trigger('fadeAnimation', [
    transition('* => *', [
      style({ position: 'relative' }),
      query(':enter, :leave', [
        style({
          position: 'absolute',
          top: 0,
          left: 0,
          width: '100%'
        })
      ]),
      query(':enter', [style({ left: '-100%', opacity: 0 })]),
      query(':leave', animateChild(), { optional: true }),
      group([
        query(':leave', [animate('1s ease-out', style({ left: '100%', opacity: 0 }))], { optional: true }),
        query(':enter', [animate('1s ease-out', style({ left: '0%', opacity: 1 }))])
      ]),
      query(':enter', animateChild())
    ])
  ]),

  trigger('scaleAnimation', [
    transition('* => *', [
        query(':enter',
            [
                style({ opacity: 0, transform: 'scale(1.1)' })
            ],
            { optional: true }
        ),
        query(':leave',
            [
                style({ opacity: 1, transform: 'scale(1)' }),
                animate('300ms ease-in-out', style({ opacity: 0, transform: 'scale(1.1)', transformOrigin: 'center' }))
            ],
            { optional: true }
        ),
        query(':enter',
            [
                style({ opacity: 0, transform: 'scale(1.1)' }),
                animate('300ms ease-in-out', style({ opacity: 1, transform: 'scale(1)', transformOrigin: 'center' }))
            ],
            { optional: true }
        )
    ])
]),

trigger('fadeInAnimation', [
  transition(':enter', [
    style({ opacity: 0}),
    animate('0.5s',
      style({ opacity: 1}))  // final
  ]),
  transition(':leave', [
    style({ opacity: 1 }),
    animate('0.2s',
      style({ opacity: 0 }))  // final
  ])
]),

trigger('toggleBox', [
  state('open', style({ height: '*', overflow: 'hidden' })),
  state('closed', style({ height: '0px', overflow: 'hidden', opacity: '0' })),
  transition('closed <=> open', animate('250ms ease-out'))
])

];
