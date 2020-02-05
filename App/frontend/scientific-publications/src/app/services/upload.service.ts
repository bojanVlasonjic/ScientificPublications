import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private editorSubject: Subject<string>;
  private fileSubject: Subject<any>;


  constructor() { 
    this.editorSubject = new Subject<string>();
    this.fileSubject = new Subject<any>();
  }

  sendEditorContent(xmlContent: string) {
    this.editorSubject.next(xmlContent);
  }

  getEditorContent(): Observable<any> {
    return this.editorSubject.asObservable();
  }

  sendFile(file: any) {
    this.fileSubject.next(file);
  }

  getFile(): Observable<any> {
    return this.fileSubject.asObservable();
  }


}
