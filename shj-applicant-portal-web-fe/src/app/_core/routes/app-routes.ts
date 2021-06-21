import { Routes } from '@angular/router';

export const APP_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('@pages/pages.module').then(m => m.PagesModule)
  },
  {
    path: '',
    loadChildren: () => import('@pages/my-profile/my-profile.module').then(m => m.MyProfileModule)
  },
  {
    path: '',
    loadChildren: () => import('@pages/home/home.module').then(m => m.HomeModule)
  },

  {
    path: 'hajj-rituals',
    loadChildren: () => import('@pages/hajj-rituals/hajj-rituals.module').then(m => m.HajjRitualsModule)
  },
  {
    path: 'hajj-journey',
    loadChildren: () => import('@pages/hajj-timeline/hajj-timeline.module').then(m => m.HajjTimelineModule)
  }
];
