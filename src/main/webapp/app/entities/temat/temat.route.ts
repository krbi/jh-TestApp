import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Temat } from 'app/shared/model/temat.model';
import { TematService } from './temat.service';
import { TematComponent } from './temat.component';
import { TematDetailComponent } from './temat-detail.component';
import { TematUpdateComponent } from './temat-update.component';
import { ITemat } from 'app/shared/model/temat.model';

@Injectable({ providedIn: 'root' })
export class TematResolve implements Resolve<ITemat> {
  constructor(private service: TematService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITemat> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((temat: HttpResponse<Temat>) => temat.body));
    }
    return of(new Temat());
  }
}

export const tematRoute: Routes = [
  {
    path: '',
    component: TematComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Temats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TematDetailComponent,
    resolve: {
      temat: TematResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Temats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TematUpdateComponent,
    resolve: {
      temat: TematResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Temats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TematUpdateComponent,
    resolve: {
      temat: TematResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Temats'
    },
    canActivate: [UserRouteAccessService]
  }
];
