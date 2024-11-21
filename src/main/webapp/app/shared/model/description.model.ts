export interface IDescription {
  id?: number;
  name?: string;
  descriptionEn?: string | null;
  descriptionId?: string | null;
}

export const defaultValue: Readonly<IDescription> = {};
