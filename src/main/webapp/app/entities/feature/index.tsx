import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Feature from './feature';
import FeatureDetail from './feature-detail';
import FeatureUpdate from './feature-update';
import FeatureDeleteDialog from './feature-delete-dialog';

const FeatureRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Feature />} />
    <Route path="new" element={<FeatureUpdate />} />
    <Route path=":id">
      <Route index element={<FeatureDetail />} />
      <Route path="edit" element={<FeatureUpdate />} />
      <Route path="delete" element={<FeatureDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeatureRoutes;
