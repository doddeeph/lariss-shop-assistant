export interface IFeature {
  id?: number;
  name?: string;
  featureEn?: string | null;
  featureId?: string | null;
}

export const defaultValue: Readonly<IFeature> = {};
