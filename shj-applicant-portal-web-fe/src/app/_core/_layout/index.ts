import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { LoginLayoutComponent } from './dcc-layout-login/login-layout.component';
import { AppLayoutComponent } from './dcc-layout-app/app-layout.component';
import { SideNavComponent } from './side-nav/side-nav.component';
import { DccLayoutLandingComponent } from './dcc-layout-landing/dcc-layout-landing.component';
import { RegisterLayoutComponent } from './dcc-register-layout/register-layout.component';
import { BiLoginLayoutComponent } from './bi-layout-login/login-layout.component';


export const layout: any[] = [FooterComponent, HeaderComponent, AppLayoutComponent, LoginLayoutComponent, SideNavComponent, BiLoginLayoutComponent,
  DccLayoutLandingComponent,RegisterLayoutComponent];

export * from './footer/footer.component';
export * from './header/header.component';
export * from './dcc-layout-login/login-layout.component';
export * from './dcc-layout-app/app-layout.component';
export * from './side-nav/side-nav.component';
export * from './dcc-layout-landing/dcc-layout-landing.component';
export * from './dcc-register-layout/register-layout.component'
export * from './bi-layout-login/login-layout.component';

