import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { DocumentUploadComponent } from './components/document-upload/document-upload.component';
import { EditorModule } from './components/document-editor/editor.module';
import { DocumentsComponent } from './components/documents/documents.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { HomeModule } from './components/home/home.module';
import { CreateSubmitionComponent } from './components/create-submition/create-submition.component';
import { FormsModule } from '@angular/forms';
import { MyScientificPapersComponent } from './components/my-scientific-papers/my-scientific-papers.component';
import { EditorComponent } from './components/editor/editor.component';
import { ScientificPapersViewComponent } from './components/scientific-papers-view/scientific-papers-view.component';
import { DocumentDownloadComponent } from './components/document-download/document-download.component';
import { PendingReviewsComponent } from './components/pending-reviews/pending-reviews.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    DocumentUploadComponent,
    DocumentsComponent,
    CreateSubmitionComponent,
    MyScientificPapersComponent,
    EditorComponent,
    ScientificPapersViewComponent,
    DocumentDownloadComponent
    PendingReviewsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    EditorModule,
    HomeModule,
    BrowserAnimationsModule

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
