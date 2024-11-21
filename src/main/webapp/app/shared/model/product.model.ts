import { ICategory } from 'app/shared/model/category.model';
import { IDescription } from 'app/shared/model/description.model';
import { IFeature } from 'app/shared/model/feature.model';
import { IBoxContent } from 'app/shared/model/box-content.model';
import { IWarranty } from 'app/shared/model/warranty.model';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { Currency } from 'app/shared/model/enumerations/currency.model';
import { Color } from 'app/shared/model/enumerations/color.model';
import { Processor } from 'app/shared/model/enumerations/processor.model';
import { Memory } from 'app/shared/model/enumerations/memory.model';
import { Storage } from 'app/shared/model/enumerations/storage.model';

export interface IProduct {
  id?: number;
  name?: string;
  sku?: string;
  basePrice?: number;
  discountPrice?: number | null;
  discountAmount?: number | null;
  discountType?: keyof typeof DiscountType | null;
  currency?: keyof typeof Currency;
  color?: keyof typeof Color;
  processor?: keyof typeof Processor;
  memory?: keyof typeof Memory;
  storage?: keyof typeof Storage;
  category?: ICategory | null;
  description?: IDescription | null;
  feature?: IFeature | null;
  boxContent?: IBoxContent | null;
  warranty?: IWarranty | null;
}

export const defaultValue: Readonly<IProduct> = {};
