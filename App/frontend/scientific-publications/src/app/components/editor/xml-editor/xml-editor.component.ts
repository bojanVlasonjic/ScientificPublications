import 'brace/index';
import 'brace/mode/xml';
import 'brace/theme/monokai';
import 'brace/theme/eclipse';
import 'brace/theme/cobalt';

import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';

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
  @Input() errorMessage: string;

  @Output() validationEvent = new EventEmitter();
  @Output() uploadEvent = new EventEmitter(); 

  constructor() { }

  ngOnInit() {
    this.documentValid = true;
    this.errorMessage = '';
    this.themes = ['monokai', 'eclipse', 'cobalt'];
    this.selectedTheme = this.themes[0];
  }

  textChanged() {
    this.validationEvent.emit(this.xmlContent);
  }

  uploadDocument() {
    this.uploadEvent.emit(this.xmlContent);
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
