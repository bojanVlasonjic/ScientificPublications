import { Component, OnInit, Input } from '@angular/core';
import { DocumentDTO } from 'src/app/models/document-dto';
import { ScientificPaperService } from 'src/app/services/scientific-paper.service';

@Component({
  selector: 'app-scientific-paper-editor',
  templateUrl: './scientific-paper-editor.component.html',
  styleUrls: ['./scientific-paper-editor.component.css']
})
export class ScientificPaperEditorComponent implements OnInit {

  template: string;
  document: DocumentDTO;

  documentValid: boolean;
  logMessage: string;

  @Input() contentToUpdate: string;

  constructor(private scientfPaperSvc: ScientificPaperService) { }

  ngOnInit() {
    this.document = new DocumentDTO();
    this.documentValid = true;
    this.logMessage = '';

    this.getTemplate();
  }

  getTemplate() {
    this.scientfPaperSvc.generateTemplate().subscribe(
      data => {
        this.template = data.documentContent;
      },
      error => {
        console.log(error.error);
      }
    );
  }

  validateDocument(event: any) {
    this.document.documentContent = event;

    this.scientfPaperSvc.validate(this.document).subscribe(
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

}
