import { TestBed } from '@angular/core/testing';

import { PaperReviewService } from './paper-review.service';

describe('PaperReviewService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PaperReviewService = TestBed.get(PaperReviewService);
    expect(service).toBeTruthy();
  });
});
