import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DocumentDTO } from '../models/documentDTO';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  storeDocument(document: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/scientific-paper', document);
  }

  validate(data: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate-xml-file', form);
  }
}
