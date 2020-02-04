import { Component, OnInit } from '@angular/core';
import { DocumentReviewService } from 'src/app/services/document-review.service';
import { DocumentDTO } from 'src/app/models/document-dto';

@Component({
  selector: 'app-document-review-editor',
  templateUrl: './document-review-editor.component.html',
  styleUrls: ['./document-review-editor.component.css']
})
export class DocumentReviewEditorComponent implements OnInit {

  document: DocumentDTO;
  template: string;
  
  documentValid: boolean;
  logMessage: string;

  constructor(private docReviewSvc: DocumentReviewService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.logMessage = '';

    this.getTemplate();
  }

  getTemplate() {
    this.docReviewSvc.generateTemplate().subscribe(
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

    this.docReviewSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.logMessage = '';
      },
      error => {
        this.documentValid = false;
        this.logMessage = `Error at line ${error.error.lineNumber}, column ${error.error.column}, ${error.error.message}`;
      }
    );
  }

  /*
  uploadDocument($event) {
    this.document.documentContent = $event;

    this.docReviewSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
        this.logMessage = 'Document successfully uploaded';
      },
      err => {
        this.logMessage = err.error.message;
      }
    );
    
  }*/

}
