import { Component, OnInit } from '@angular/core';
import { DocumentDTO } from 'src/app/models/documentDTO';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';

@Component({
  selector: 'app-scientific-paper-editor',
  templateUrl: './scientific-paper-editor.component.html',
  styleUrls: ['./scientific-paper-editor.component.css']
})
export class ScientificPaperEditorComponent implements OnInit {

  document: DocumentDTO;
  documentValid: boolean;
  errorMessage: string;

  constructor(private scientfPaperSvc: ScientificPaperService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.errorMessage = '';
  }

  validateDocument($event) {
    this.document.documentContent = $event;

    this.scientfPaperSvc.validate(this.document).subscribe(
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

    this.scientfPaperSvc.storeDocument(this.document).subscribe(
      data => {
        this.document = data;
      },
      err => {
        console.log(err.error);
      }
    );
    
  }

}
