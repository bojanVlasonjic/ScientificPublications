import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { DocumentDTO } from '../models/documentDTO';

@Injectable({
  providedIn: 'root'
})
export class CoverLetterService {

  constructor(private http: HttpClient) { }

  storeDocument(document: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/cover-letter', document);
  }

  validate(data: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/cover-letter/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/cover-letter/validate-xml-file', form);
  }

  generateTemplate(): Observable<any> {
    return this.http.get('api/cover-letter/template');
  }
}
