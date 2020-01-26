import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  validate(data: Document): Observable<any> {
    return this.http.post<any>('api/scientific-publication/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/scientific-publication/validate-xml-file', form);
  }
}
