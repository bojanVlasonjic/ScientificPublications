import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Document } from '../models/document';

@Injectable({
  providedIn: 'root'
})
export class CoverLetterService {

  constructor(private http: HttpClient) { }

  storeDocument(document: Document): Observable<any> {
    return this.http.post<any>('api/cover-letter', document);
  }

  validate(data: Document): Observable<any> {
    return this.http.post<any>('api/cover-letter/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/cover-letter/validate-xml-file', form);
  }
}
