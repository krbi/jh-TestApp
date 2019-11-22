import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITemat } from 'app/shared/model/temat.model';
import { TematService } from './temat.service';

@Component({
  templateUrl: './temat-delete-dialog.component.html'
})
export class TematDeleteDialogComponent {
  temat: ITemat;

  constructor(protected tematService: TematService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tematService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'tematListModification',
        content: 'Deleted an temat'
      });
      this.activeModal.dismiss(true);
    });
  }
}
