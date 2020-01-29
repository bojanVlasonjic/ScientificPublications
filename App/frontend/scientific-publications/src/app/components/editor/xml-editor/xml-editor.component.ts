import 'ace-builds/src-min-noconflict/ace';
import 'brace/mode/xml';

import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';

@Component({
  selector: 'app-xml-editor',
  templateUrl: './xml-editor.component.html',
  styleUrls: ['./xml-editor.component.css']
})
export class XmlEditorComponent implements OnInit {

  xmlContent: string;
  options:any = {maxLines: 1000, printMargin: false, showInvisibles: false};

  @Input() documentValid: boolean;
  @Input() errorMessage: string;

  @Output() validationEvent = new EventEmitter();
  @Output() uploadEvent = new EventEmitter(); 

  constructor() { }

  ngOnInit() {
    this.documentValid = true;
    this.errorMessage = '';
  }

  textChanged() {
    this.validationEvent.emit(this.xmlContent);
  }

  uploadDocument() {
    this.uploadEvent.emit(this.xmlContent);
  }
  

}
