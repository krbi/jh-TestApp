import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'program',
        loadChildren: () => import('./program/program.module').then(m => m.JhTestAppProgramModule)
      },
      {
        path: 'temat',
        loadChildren: () => import('./temat/temat.module').then(m => m.JhTestAppTematModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JhTestAppEntityModule {}
