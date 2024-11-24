import { IColor } from 'app/shared/model/color.model';
import { IProcessor } from 'app/shared/model/processor.model';
import { IMemory } from 'app/shared/model/memory.model';
import { IStorage } from 'app/shared/model/storage.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IVariant {
  id?: number;
  label?: string | null;
  sku?: string | null;
  color?: IColor | null;
  processor?: IProcessor | null;
  memory?: IMemory | null;
  storage?: IStorage | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<IVariant> = {};
