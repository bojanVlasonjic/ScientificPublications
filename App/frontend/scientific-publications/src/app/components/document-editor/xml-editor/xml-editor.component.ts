import 'brace/index';
import 'brace/mode/xml';
import 'brace/theme/monokai';
import 'brace/theme/eclipse';
import 'brace/theme/cobalt';

import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { UploadService } from 'src/app/services/upload.service';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-xml-editor',
  templateUrl: './xml-editor.component.html',
  styleUrls: ['./xml-editor.component.css']
})
export class XmlEditorComponent implements OnInit {

  xmlContent: string;
  options:any = {maxLines: 1000, printMargin: false, showInvisibles: false};
  themes: Array<string>;
  selectedTheme: string;

  @Input() template: string;
  @Input() documentValid: boolean;
  @Input() logMessage: string;

  @Output() validationEvent = new EventEmitter();

  constructor(private uploadSvc: UploadService, private toastSvc: ToasterService) { }

  ngOnInit() {
    this.documentValid = true;
    this.logMessage = '';
    this.themes = ['monokai', 'eclipse', 'cobalt'];
    this.selectedTheme = this.themes[0];
  }

  textChanged() {
    this.validationEvent.emit(this.xmlContent);
  }

  uploadDocument() {
    if(!this.documentValid || this.xmlContent == '' || this.xmlContent == null) {
      this.toastSvc.showMessage('Not valid', 'Document is not valid and can not be uploaded');
      return;
    }

    // waiting for editor content to update
    window.setTimeout(() =>{
    }, 1500);

    this.uploadSvc.sendEditorContent(this.xmlContent);
  }

  generateTemplate() {
    if(this.template != null) {
      this.xmlContent = this.template;
    }
    
  }

  changeTheme(event: any) {
    this.selectedTheme = this.themes[event.target.value];
  }
  

}
