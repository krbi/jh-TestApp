import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ITemat, Temat } from 'app/shared/model/temat.model';
import { TematService } from './temat.service';
import { IProgram } from 'app/shared/model/program.model';
import { ProgramService } from 'app/entities/program/program.service';

@Component({
  selector: 'jhi-temat-update',
  templateUrl: './temat-update.component.html'
})
export class TematUpdateComponent implements OnInit {
  isSaving: boolean;

  programs: IProgram[];

  editForm = this.fb.group({
    id: [],
    nazwa: [],
    program: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tematService: TematService,
    protected programService: ProgramService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ temat }) => {
      this.updateForm(temat);
    });
    this.programService
      .query()
      .subscribe((res: HttpResponse<IProgram[]>) => (this.programs = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(temat: ITemat) {
    this.editForm.patchValue({
      id: temat.id,
      nazwa: temat.nazwa,
      program: temat.program
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const temat = this.createFromForm();
    if (temat.id !== undefined) {
      this.subscribeToSaveResponse(this.tematService.update(temat));
    } else {
      this.subscribeToSaveResponse(this.tematService.create(temat));
    }
  }

  private createFromForm(): ITemat {
    return {
      ...new Temat(),
      id: this.editForm.get(['id']).value,
      nazwa: this.editForm.get(['nazwa']).value,
      program: this.editForm.get(['program']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemat>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProgramById(index: number, item: IProgram) {
    return item.id;
  }
}
