import { TestBed } from '@angular/core/testing';

import { XmlEditorService } from './xml-editor.service';

describe('XmlEditorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: XmlEditorService = TestBed.get(XmlEditorService);
    expect(service).toBeTruthy();
  });
});
