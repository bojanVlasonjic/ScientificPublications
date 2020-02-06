import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PaperReviewService {

  constructor(private http: HttpClient) { }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/paper-review/validate-xml-file', form);
  }

  uploadFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/paper-review/upload-xml-file', form);
  }
  
}
