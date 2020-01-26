import { Component, OnInit } from '@angular/core';
import 'ace-builds/src-min-noconflict/ace';
import 'brace/mode/xml';

@Component({
  selector: 'app-xml-editor',
  templateUrl: './xml-editor.component.html',
  styleUrls: ['./xml-editor.component.css']
})
export class XmlEditorComponent implements OnInit {

  xmlContent: string = '';
  options:any = {maxLines: 1000, printMargin: false};

  constructor() { }

  ngOnInit() {
    this.xmlContent = '<xml> Your xml code goes here </xml>'
  }

  textChanged() {
    
  }

}
