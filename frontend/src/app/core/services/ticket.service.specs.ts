import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TicketService } from './ticket.service';
import { Ticket, TicketType } from '../models/ticket.model';

describe('TicketService', () => {
  let service: TicketService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TicketService]
    });

    service = TestBed.inject(TicketService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); 
  });

  it('should fetch ticket types', () => {
    const mockTypes: TicketType[] = [
      {
        id: 1,
        name: 'Bilet 20-minutowy',
        category: 'TIME',
        discountType: 'NORMAL',
        price: 3.4,
        durationMinutes: 20
      }
    ];

    service.getTicketTypes().subscribe(types => {
      expect(types).toEqual(mockTypes);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tickets/types');
    expect(req.request.method).toBe('GET');
    req.flush(mockTypes);
  });

  it('should fetch my tickets', () => {
    const mockTickets: Ticket[] = [
      {
        id: 1,
        ticketType: {
          id: 1,
          name: 'Bilet 20-minutowy',
          category: 'TIME',
          discountType: 'NORMAL',
          price: 3.4,
          durationMinutes: 20
        },
        purchaseDate: new Date().toISOString(),
        used: false
      }
    ];

    service.getMyTickets().subscribe(tickets => {
      expect(tickets).toEqual(mockTickets);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tickets/my');
    expect(req.request.method).toBe('GET');
    req.flush(mockTickets);
  });

  it('should buy a ticket', () => {
    const mockTicket: Ticket = {
      id: 2,
      ticketType: {
        id: 1,
        name: 'Bilet 20-minutowy',
        category: 'TIME',
        discountType: 'NORMAL',
        price: 3.4,
        durationMinutes: 20
      },
      purchaseDate: new Date().toISOString(),
      used: false
    };

    service.buyTicket(1).subscribe(ticket => {
      expect(ticket).toEqual(mockTicket);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tickets/buy');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ ticketTypeId: 1 });
    req.flush(mockTicket);
  });

  it('should activate a ticket', () => {
    const mockResponse = { success: true };

    service.activateTicket('ABC123', 'BUS42').subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tickets/validate/activate');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      ticketCode: 'ABC123',
      vehicleId: 'BUS42'
    });
    req.flush(mockResponse);
  });
});
