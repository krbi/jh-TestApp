import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhTestAppTestModule } from '../../../test.module';
import { TematDetailComponent } from 'app/entities/temat/temat-detail.component';
import { Temat } from 'app/shared/model/temat.model';

describe('Component Tests', () => {
  describe('Temat Management Detail Component', () => {
    let comp: TematDetailComponent;
    let fixture: ComponentFixture<TematDetailComponent>;
    const route = ({ data: of({ temat: new Temat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhTestAppTestModule],
        declarations: [TematDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TematDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TematDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.temat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
