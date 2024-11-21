import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BoxContent from './box-content';
import BoxContentDetail from './box-content-detail';
import BoxContentUpdate from './box-content-update';
import BoxContentDeleteDialog from './box-content-delete-dialog';

const BoxContentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BoxContent />} />
    <Route path="new" element={<BoxContentUpdate />} />
    <Route path=":id">
      <Route index element={<BoxContentDetail />} />
      <Route path="edit" element={<BoxContentUpdate />} />
      <Route path="delete" element={<BoxContentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BoxContentRoutes;
