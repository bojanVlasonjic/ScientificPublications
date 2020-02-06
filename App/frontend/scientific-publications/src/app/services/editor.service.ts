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

  requestRevisionsSubmition(id: number): Observable<any> {
    return this.http.put(`api/submitions/${id}/request-revision`, null);
  }

  rejectSubmition(id: number): Observable<any> {
    return this.http.put(`api/submitions/${id}/reject`, null);
  }

  startReviewForSubmition(id: number): Observable<any> {
    return this.http.put(`api/editor/set-in-review-process/${id}`, null);
  }
}
