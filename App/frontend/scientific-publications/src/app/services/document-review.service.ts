import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DocumentDTO } from '../models/documentDTO';

@Injectable({
  providedIn: 'root'
})
export class DocumentReviewService {

  constructor(private http: HttpClient) { }

  storeDocument(document: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/document-review', document);
  }

  validate(data: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/document-review/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/document-review/validate-xml-file', form);
  }

  uploadFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/document-review/upload-xml-file', form);
  }

  generateTemplate(): Observable<any> {
    return this.http.get('api/document-review/template');
  }
}
