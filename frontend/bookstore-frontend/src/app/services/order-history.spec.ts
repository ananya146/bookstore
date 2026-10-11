import { TestBed } from '@angular/core/testing';
import { OrderHistory } from './order-history';

describe('OrderHistory', () => {
  let service: OrderHistory;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderHistory);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
