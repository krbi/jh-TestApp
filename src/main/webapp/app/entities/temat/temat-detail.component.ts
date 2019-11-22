import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITemat } from 'app/shared/model/temat.model';

@Component({
  selector: 'jhi-temat-detail',
  templateUrl: './temat-detail.component.html'
})
export class TematDetailComponent implements OnInit {
  temat: ITemat;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ temat }) => {
      this.temat = temat;
    });
  }

  previousState() {
    window.history.back();
  }
}
