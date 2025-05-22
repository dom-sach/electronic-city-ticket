export type TicketCategory = 'ONE_TIME' | 'TIME' | 'PERIOD';
export type DiscountType = 'NORMAL' | 'DISCOUNT';

export interface TicketType {
  id: number;
  name: string;
  category: TicketCategory;
  discountType: DiscountType;
  price: number;
  durationMinutes?: number;
}

export interface Vehicle {
  vehicleId: string;
}

export interface Ticket {
  id: number;
  ticketType: TicketType;
  purchaseDate: string;        // ISO string
  activationDate?: string;
  validUntil?: string;
  activatedIn?: Vehicle;
  used: boolean;
}
