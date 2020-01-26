import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  storeDocument(document: Document): Observable<any> {
    return this.http.post<any>('api/scientific-paper', document);
  }

  validate(data: Document): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate-xml-file', form);
  }
}
