import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { JhTestAppSharedModule } from 'app/shared/shared.module';
import { JhTestAppCoreModule } from 'app/core/core.module';
import { JhTestAppAppRoutingModule } from './app-routing.module';
import { JhTestAppHomeModule } from './home/home.module';
import { JhTestAppEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    JhTestAppSharedModule,
    JhTestAppCoreModule,
    JhTestAppHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    JhTestAppEntityModule,
    JhTestAppAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class JhTestAppAppModule {}
