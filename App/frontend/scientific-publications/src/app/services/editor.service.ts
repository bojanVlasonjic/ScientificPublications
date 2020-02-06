import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EditorService {

  constructor(private http: HttpClient) { }

  getReviewsForSubmition(submitionId: number): Observable<any> {
    return this.http.get(`api/editor/reviews/${submitionId}`);
  }

  acceptSubmition(id: number): Observable<any> {
    return this.http.put(`api/submitions/${id}/publish`, null);
  }

  requestRevisionsSubmition(): Observable<any> {
    return this.http.put('', null);
  }

  rejectSubmition(): Observable<any> {
    return this.http.put('', null);
  }

  startReviewForSubmition(id: number): Observable<any> {
    return this.http.put('', null);
  }
}
