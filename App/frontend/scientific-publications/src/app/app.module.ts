import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { DocumentUploadComponent } from './components/document-upload/document-upload.component';
import { EditorModule } from './components/editor/editor.module';
import { DocumentsComponent } from './components/documents/documents.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { HomeModule } from './components/home/home.module';

@NgModule({
  declarations: [
    AppComponent,
    DocumentUploadComponent,
    DocumentsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    EditorModule,
    HomeModule

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
