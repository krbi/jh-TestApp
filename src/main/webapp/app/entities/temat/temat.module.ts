import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhTestAppSharedModule } from 'app/shared/shared.module';
import { TematComponent } from './temat.component';
import { TematDetailComponent } from './temat-detail.component';
import { TematUpdateComponent } from './temat-update.component';
import { TematDeleteDialogComponent } from './temat-delete-dialog.component';
import { tematRoute } from './temat.route';

@NgModule({
  imports: [JhTestAppSharedModule, RouterModule.forChild(tematRoute)],
  declarations: [TematComponent, TematDetailComponent, TematUpdateComponent, TematDeleteDialogComponent],
  entryComponents: [TematDeleteDialogComponent]
})
export class JhTestAppTematModule {}
