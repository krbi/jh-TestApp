import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhTestAppTestModule } from '../../../test.module';
import { TematUpdateComponent } from 'app/entities/temat/temat-update.component';
import { TematService } from 'app/entities/temat/temat.service';
import { Temat } from 'app/shared/model/temat.model';

describe('Component Tests', () => {
  describe('Temat Management Update Component', () => {
    let comp: TematUpdateComponent;
    let fixture: ComponentFixture<TematUpdateComponent>;
    let service: TematService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhTestAppTestModule],
        declarations: [TematUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TematUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TematUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TematService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Temat(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Temat();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
