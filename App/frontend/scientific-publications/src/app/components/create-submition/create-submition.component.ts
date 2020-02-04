import { Component, OnInit, OnDestroy } from '@angular/core';
import { CreateSubmitionDTO } from 'src/app/models/submitions/create-submition-dto.model';
import { Subscription } from 'rxjs';
import { UploadService } from 'src/app/services/upload.service';
import { SubmitionService } from 'src/app/services/submition.service';
import { ToasterService } from 'src/app/services/toaster.service';

@Component({
  selector: 'app-create-submition',
  templateUrl: './create-submition.component.html',
  styleUrls: ['./create-submition.component.css']
})
export class CreateSubmitionComponent implements OnInit, OnDestroy {

  plainSubmition: CreateSubmitionDTO;
  fileSubmition: FormData;

  uploadType: string;
  uploadCoverLetter: boolean;

  editorSubscription: Subscription;
  fileSubscription: Subscription;

  constructor(
    private toastSvc: ToasterService,
    private submitionSvc: SubmitionService,
    private uploadSvc: UploadService
    ) {

    this.plainSubmition = new CreateSubmitionDTO();
    this.fileSubmition = new FormData();
    this.resetData();
  }

  ngOnInit() {
    this.editorSubscription = this.uploadSvc.getEditorContent().subscribe(
      data => {
        if(this.uploadCoverLetter) {
          this.plainSubmition.coverLetterContent = data;
          this.uploadSubmition();
        } else {
          this.plainSubmition.paperContent = data;
          this.offerCoverLetter();
        }
      }
    );

    this.fileSubscription = this.uploadSvc.getFile().subscribe(
      data => {
        if(this.uploadCoverLetter) {
          this.fileSubmition.append('submition-files', data); // adding cover letter
          this.uploadFileSubmition();
        } else {
          this.fileSubmition.append('submition-files', data); //adding scientific paper
          this.offerCoverLetter();
        }
      }
    );

  }

  offerCoverLetter() {

    if(window.confirm('Would you like to upload a cover letter as well?')) {
      this.uploadCoverLetter = true;
    } else {
      this.uploadSubmition();
    }

  }

  uploadSubmition() {

    if(this.plainSubmition.paperContent != "") {
      this.uploadPlainSubmition();
    } else if(this.fileSubmition.get('submition-files') != null) {
      this.uploadFileSubmition();
    } else {
      this.toastSvc.showMessage('Something went wrong', 'Please refresh the page');
    }

  }

  uploadPlainSubmition() {
    
    this.submitionSvc.uploadSubmition(this.plainSubmition).subscribe(
      data => {
        this.toastSvc.showMessage('Success', 'Submition successfully uploaded');
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    ).add(
      () => {
        this.resetData();
      }
    );

  }

  uploadFileSubmition() {
    
    this.submitionSvc.uploadSubmitionFile(this.fileSubmition).subscribe(
      data => {
        this.toastSvc.showMessage('Success', 'Submition successfully uploaded');
      },
      err => {
        this.toastSvc.showErrorMessage(err);
      }
    ).add(
      () => {
        this.resetData();
      }
    ); 

  }

  resetData() {
    this.uploadCoverLetter = false;
    this.uploadType = "";
    this.plainSubmition.coverLetterContent = "";
    this.plainSubmition.paperContent = "";
    this.fileSubmition.set('submition-files', null);
  }

  ngOnDestroy() {
    this.editorSubscription.unsubscribe();
    this.fileSubscription.unsubscribe();
  }


}
