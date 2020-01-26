import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document } from '../models/document';

@Injectable({
  providedIn: 'root'
})
export class DocumentReviewService {

  constructor(private http: HttpClient) { }

  validate(data: Document): Observable<any> {
    return this.http.post<any>('api/document-review/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/document-review/validate-xml-file', form);
  }
}
