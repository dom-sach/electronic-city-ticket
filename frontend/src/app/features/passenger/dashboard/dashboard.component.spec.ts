import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { of, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TicketService } from '../../../core/services/ticket.service';
import { Ticket, TicketType, TicketCategory, DiscountType } from '../../../core/models/ticket.model';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let ticketServiceSpy: jasmine.SpyObj<TicketService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;

  const mockTicketTypes: TicketType[] = [
    {
      id: 1,
      name: 'Normalny',
      category: 'ONE_TIME' as TicketCategory,
      discountType: 'NORMAL' as DiscountType,
      price: 10
    }
  ];

  const mockTickets: Ticket[] = [
    {
      id: 1,
      ticketType: mockTicketTypes[0],
      purchaseDate: new Date().toISOString(),
      used: false
    }
  ];

  beforeEach(async () => {
    ticketServiceSpy = jasmine.createSpyObj('TicketService', [
      'getTicketTypes',
      'getMyTickets',
      'buyTicket'
    ]);

    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);

    await TestBed.configureTestingModule({
      imports: [DashboardComponent, HttpClientTestingModule],
      providers: [
        { provide: TicketService, useValue: ticketServiceSpy },
        { provide: MatDialog, useValue: dialogSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load ticket types and user tickets on init', () => {
    ticketServiceSpy.getTicketTypes.and.returnValue(of(mockTicketTypes));
    ticketServiceSpy.getMyTickets.and.returnValue(of(mockTickets));

    component.ngOnInit();

    expect(ticketServiceSpy.getTicketTypes).toHaveBeenCalled();
    expect(ticketServiceSpy.getMyTickets).toHaveBeenCalled();
    expect(component.tickets.length).toBe(1);
    expect(component.myTickets.length).toBe(1);
  });

  it('should handle buy ticket success', () => {
  ticketServiceSpy.getTicketTypes.and.returnValue(of([]));
  ticketServiceSpy.getMyTickets.and.returnValue(of([]));
  ticketServiceSpy.buyTicket.and.returnValue(of({
    id: 2,
    ticketType: mockTicketTypes[0],
    purchaseDate: new Date().toISOString(),
    used: false
  }));

  spyOn(window, 'alert');

  component.ngOnInit();
  component.buy(1);

  expect(ticketServiceSpy.buyTicket).toHaveBeenCalledWith(1);
  expect(window.alert).toHaveBeenCalledWith('Bilet zakupiony!');
});

  it('should return ticket status correctly', () => {
    const now = new Date();
    const expiredDate = new Date(now.getTime() - 10000).toISOString();
    const futureDate = new Date(now.getTime() + 100000).toISOString();

    expect(component.getTicketStatus({ used: true })).toBe('Skasowany');
    expect(component.getTicketStatus({ used: false, validUntil: expiredDate })).toBe('Wygasły');
    expect(component.getTicketStatus({ used: false, validUntil: futureDate })).toBe('Aktywny');
  });
});
