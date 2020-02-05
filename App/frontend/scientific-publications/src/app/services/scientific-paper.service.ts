import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DocumentDTO } from '../models/document-dto';

@Injectable({
  providedIn: 'root'
})
export class ScientificPaperService {

  constructor(private http: HttpClient) { }

  getRecommenderReviewers(paperId: string): Observable<any> {
    return this.http.get(`api/scientific-paper/${paperId}/recommended-reviewers`)
  }

  storeDocument(document: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/scientific-paper', document);
  }

  validate(data: DocumentDTO): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate', data);
  }

  validateFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/scientific-paper/validate-xml-file', form);
  }

  uploadFile(form: FormData): Observable<any> {
    return this.http.post<any>('api/scientific-paper/upload-xml-file', form);
  }

  generateTemplate(): Observable<any> {
    return this.http.get('api/scientific-paper/template');
  }
  
  searchByAuthorsMetadata(author: string): Observable<any> {
    return this.http.post<any>('api/scientific-paper/rdf/search/by-authors', {
      'author': author
    });
  }

}
