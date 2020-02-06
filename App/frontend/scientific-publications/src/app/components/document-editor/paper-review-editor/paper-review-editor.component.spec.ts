import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperReviewEditorComponent } from './paper-review-editor.component';

describe('PaperReviewEditorComponent', () => {
  let component: PaperReviewEditorComponent;
  let fixture: ComponentFixture<PaperReviewEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PaperReviewEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PaperReviewEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
