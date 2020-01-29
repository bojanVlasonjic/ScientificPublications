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
  documentValid: boolean;
  errorMessage: string;

  constructor(private coverLetterSvc: CoverLetterService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.errorMessage = '';
  }

  validateDocument($event) {
    this.document.documentContent = $event;

    this.coverLetterSvc.validate(this.document).subscribe(
      data => {
        this.documentValid = data;
        this.errorMessage = '';
      },
      error => {
        this.documentValid = false;
        this.errorMessage = `Error at line ${error.error.lineNumber}, column ${error.error.column}, ${error.error.message}`;
      }
    );
  }


  uploadDocument($event) {
    this.document.documentContent = $event;

    this.coverLetterSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      },
      err => {
        console.log(err.error);
      }
    );
    
  }


}
