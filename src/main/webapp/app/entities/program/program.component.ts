import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProgram } from 'app/shared/model/program.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProgramService } from './program.service';
import { ProgramDeleteDialogComponent } from './program-delete-dialog.component';

@Component({
  selector: 'jhi-program',
  templateUrl: './program.component.html'
})
export class ProgramComponent implements OnInit, OnDestroy {
  programs: IProgram[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected programService: ProgramService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.programs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.programService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IProgram[]>) => this.paginatePrograms(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.programs = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInPrograms();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProgram) {
    return item.id;
  }

  registerChangeInPrograms() {
    this.eventSubscriber = this.eventManager.subscribe('programListModification', () => this.reset());
  }

  delete(program: IProgram) {
    const modalRef = this.modalService.open(ProgramDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.program = program;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePrograms(data: IProgram[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.programs.push(data[i]);
    }
  }
}
