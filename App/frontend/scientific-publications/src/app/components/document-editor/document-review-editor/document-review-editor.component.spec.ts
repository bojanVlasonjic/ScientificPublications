import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentReviewEditorComponent } from './document-review-editor.component';

describe('DocumentReviewEditorComponent', () => {
  let component: DocumentReviewEditorComponent;
  let fixture: ComponentFixture<DocumentReviewEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentReviewEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentReviewEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
