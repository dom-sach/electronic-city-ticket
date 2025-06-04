import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketActivateComponent } from './ticket-activate.component';

describe('TicketActivateComponent', () => {
  let component: TicketActivateComponent;
  let fixture: ComponentFixture<TicketActivateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketActivateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketActivateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
