import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class XmlEditorService {

  private validationSubject: Subject<string>;

  constructor() { 
    this.validationSubject = new Subject<string>();
  }


  public sendValidationResult(result: any) {
    this.validationSubject.next(result);
  }

  public getValidationResult(): Observable<any> {
    return this.validationSubject.asObservable();
  }

}
