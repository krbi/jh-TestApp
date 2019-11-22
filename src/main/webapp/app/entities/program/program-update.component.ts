import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IProgram, Program } from 'app/shared/model/program.model';
import { ProgramService } from './program.service';

@Component({
  selector: 'jhi-program-update',
  templateUrl: './program-update.component.html'
})
export class ProgramUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    nazwa: [],
    opis: []
  });

  constructor(protected programService: ProgramService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ program }) => {
      this.updateForm(program);
    });
  }

  updateForm(program: IProgram) {
    this.editForm.patchValue({
      id: program.id,
      nazwa: program.nazwa,
      opis: program.opis
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const program = this.createFromForm();
    if (program.id !== undefined) {
      this.subscribeToSaveResponse(this.programService.update(program));
    } else {
      this.subscribeToSaveResponse(this.programService.create(program));
    }
  }

  private createFromForm(): IProgram {
    return {
      ...new Program(),
      id: this.editForm.get(['id']).value,
      nazwa: this.editForm.get(['nazwa']).value,
      opis: this.editForm.get(['opis']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgram>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
