import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateSubmitionDTO } from '../models/submitions/create-submition-dto.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubmitionService {

  constructor(private http: HttpClient) { }

  getSubmitionsForAuthor(): Observable<any> {
    return this.http.get('api/submitions/author');
  }

  uploadSubmition(submitionData: CreateSubmitionDTO): Observable<any> {
    return this.http.post('api/submitions', submitionData);
  }

  uploadSubmitionFile(submitionFiles: FormData): Observable<any> {
    return this.http.post('api/submitions/file', submitionFiles);
  }
}
