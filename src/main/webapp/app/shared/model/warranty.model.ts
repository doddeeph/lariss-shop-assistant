export interface IWarranty {
  id?: number;
  name?: string;
  warrantyEn?: string | null;
  warrantyId?: string | null;
}

export const defaultValue: Readonly<IWarranty> = {};
