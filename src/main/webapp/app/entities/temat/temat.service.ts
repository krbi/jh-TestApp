import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITemat } from 'app/shared/model/temat.model';

type EntityResponseType = HttpResponse<ITemat>;
type EntityArrayResponseType = HttpResponse<ITemat[]>;

@Injectable({ providedIn: 'root' })
export class TematService {
  public resourceUrl = SERVER_API_URL + 'api/temats';

  constructor(protected http: HttpClient) {}

  create(temat: ITemat): Observable<EntityResponseType> {
    return this.http.post<ITemat>(this.resourceUrl, temat, { observe: 'response' });
  }

  update(temat: ITemat): Observable<EntityResponseType> {
    return this.http.put<ITemat>(this.resourceUrl, temat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITemat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITemat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
