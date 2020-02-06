import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReviewersService {

  constructor(private http: HttpClient) { }

  getPendingReviewsForCurrentReviewer(): Observable<any> {
    return this.http.get('api/reviewers/pending/reviews');
  }

  acceptSubmitionReviewRequest(id: number): Observable<any> {
    return this.http.put<any>(`api/reviewers/submitions/${id}/accept`, null);
  }

  rejectSubmitionReviewRequest(id: number): Observable<any> {
    return this.http.put<any>(`api/reviewers/submitions/${id}/reject`, null);
  }

  
}
