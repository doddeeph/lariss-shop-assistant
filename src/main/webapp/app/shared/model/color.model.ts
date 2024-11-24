export interface IColor {
  id?: number;
  attributeLabel?: string | null;
  attributeName?: string | null;
  optionLabel?: string | null;
  optionValue?: string | null;
}

export const defaultValue: Readonly<IColor> = {};
