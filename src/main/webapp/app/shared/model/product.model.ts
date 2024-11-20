import { ICategory } from 'app/shared/model/category.model';
import { Color } from 'app/shared/model/enumerations/color.model';
import { Processor } from 'app/shared/model/enumerations/processor.model';
import { Memory } from 'app/shared/model/enumerations/memory.model';
import { Storage } from 'app/shared/model/enumerations/storage.model';

export interface IProduct {
  id?: number;
  name?: string;
  sku?: string;
  price?: number;
  currency?: string;
  color?: keyof typeof Color;
  processor?: keyof typeof Processor;
  memory?: keyof typeof Memory;
  storage?: keyof typeof Storage;
  description?: string | null;
  feature?: string | null;
  boxContents?: string | null;
  warranty?: string | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IProduct> = {};
