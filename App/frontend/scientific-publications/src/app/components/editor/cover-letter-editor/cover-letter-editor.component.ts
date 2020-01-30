import { Component, OnInit } from '@angular/core';
import { CoverLetterService } from 'src/app/services/cover-letter.service';
import { DocumentDTO } from 'src/app/models/documentDTO';

@Component({
  selector: 'app-cover-letter-editor',
  templateUrl: './cover-letter-editor.component.html',
  styleUrls: ['./cover-letter-editor.component.css']
})
export class CoverLetterEditorComponent implements OnInit {

  document: DocumentDTO;
  template: string;
  
  documentValid: boolean;
  logMessage: string;

  constructor(private coverLetterSvc: CoverLetterService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.logMessage = '';

    this.getTemplate();
  }

  getTemplate() {
    this.coverLetterSvc.generateTemplate().subscribe(
      data => {
        this.template = data.documentContent;
      },
      error => {
        console.log(error.error);
      }
    );
  }

  validateDocument($event) {
    this.document.documentContent = $event;

    this.coverLetterSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
        this.logMessage = 'Document successfully uploaded';
      },
      err => {
        this.logMessage = err.error.message;
      }
    );

  }


  uploadDocument($event) {
    this.document.documentContent = $event;

    this.coverLetterSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
        this.logMessage = 'Document successfully uploaded';
      },
      err => {
        this.logMessage = err.error.message;
      }
    );
    
  }


}
