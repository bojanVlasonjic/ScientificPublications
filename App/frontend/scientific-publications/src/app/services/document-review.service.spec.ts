import { TestBed } from '@angular/core/testing';

import { DocumentReviewService } from './document-review.service';

describe('DocumentReviewService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DocumentReviewService = TestBed.get(DocumentReviewService);
    expect(service).toBeTruthy();
  });
});
