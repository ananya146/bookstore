import { TestBed } from '@angular/core/testing';
import { Recommendation } from './recommendation';

describe('Recommendation', () => {
  let service: Recommendation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Recommendation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
