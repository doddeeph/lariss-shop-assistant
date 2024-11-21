export interface IBoxContent {
  id?: number;
  name?: string;
  contentEn?: string | null;
  contentId?: string | null;
}

export const defaultValue: Readonly<IBoxContent> = {};
