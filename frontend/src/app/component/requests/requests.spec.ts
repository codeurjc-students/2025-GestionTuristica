import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Requests } from './requests';
import { provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { RequestService } from '../../services/request.service';

describe('Requests', () => {
  let component: Requests;
  let fixture: ComponentFixture<Requests>;
  let requestServiceMock: any;

  const mockRequests = [
    {
      id: 1,
      status: 'PENDING',
      reservationIdentifier: 'RSV-123',
      type: 'CANCELLATION',
      userEmail: 'john@test.com'
    },
    {
      id: 2,
      status: 'PENDING',
      reservationIdentifier: 'RSV-456',
      type: 'MODIFICATION',
      requestedStartDate: '2026-02-24',
      requestedEndDate: '2026-02-27',
      userEmail: 'john@test.com'
    }
  ]

  beforeEach(async () => {

    requestServiceMock = {
      getRequests: jasmine.createSpy('getRequests').and.returnValue(of(mockRequests)),
      approveRequest: jasmine.createSpy('approveRequest').and.returnValue(of({})),
      rejectRequest: jasmine.createSpy('rejectRequest').and.returnValue(of({}))
    }

    await TestBed.configureTestingModule({
      imports: [Requests],
      providers: [
        provideHttpClient(),
        { provide: RequestService, useValue: requestServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Requests);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load requests on init', () => {
    expect(component.requests.length).toBe(2);
    expect(requestServiceMock.getRequests).toHaveBeenCalled();
  });

  it('should call request service on request approve', () => {
    component.approveRequest(1);
    expect(requestServiceMock.approveRequest).toHaveBeenCalledWith(1);
  });

  it('should call request service on request reject', () => {
    component.rejectRequest(1);
    expect(requestServiceMock.rejectRequest).toHaveBeenCalledWith(1);
  });
});
