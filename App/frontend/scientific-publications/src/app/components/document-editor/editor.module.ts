import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { XmlEditorComponent } from './xml-editor/xml-editor.component';
import { CoverLetterEditorComponent } from './cover-letter-editor/cover-letter-editor.component';
import { ScientificPaperEditorComponent } from './scientific-paper-editor/scientific-paper-editor.component';
import { DocumentReviewEditorComponent } from './document-review-editor/document-review-editor.component';
import { AceEditorModule } from 'ng2-ace-editor';
import { HttpClientModule } from '@angular/common/http';
import { PaperReviewEditorComponent } from './paper-review-editor/paper-review-editor.component';

@NgModule({
    declarations: [
        XmlEditorComponent,
        CoverLetterEditorComponent,
        ScientificPaperEditorComponent,
        DocumentReviewEditorComponent,
        PaperReviewEditorComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AceEditorModule
    ],
    exports: [
        CoverLetterEditorComponent,
        ScientificPaperEditorComponent,
        DocumentReviewEditorComponent,
        PaperReviewEditorComponent
    ]
})

export class EditorModule { }
