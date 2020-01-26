import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HttpClientModule } from '@angular/common/http';
import { DocumentUploadComponent } from './components/document-upload/document-upload.component';
import { XmlEditorComponent } from './components/xml-editor/xml-editor.component';
import { AceEditorModule } from 'ng2-ace-editor';
import { DocumentEditorComponent } from './components/document-editor/document-editor.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    DocumentUploadComponent,
    XmlEditorComponent,
    DocumentEditorComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AceEditorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
