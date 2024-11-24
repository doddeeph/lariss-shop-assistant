import { IVariant } from 'app/shared/model/variant.model';

export interface IProduct {
  id?: number;
  name?: string;
  sku?: string;
  price?: number;
  specialPrice?: number;
  quantity?: number;
  thumbnail?: string;
  variants?: IVariant[] | null;
}

export const defaultValue: Readonly<IProduct> = {};
