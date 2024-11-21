import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Warranty from './warranty';
import WarrantyDetail from './warranty-detail';
import WarrantyUpdate from './warranty-update';
import WarrantyDeleteDialog from './warranty-delete-dialog';

const WarrantyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Warranty />} />
    <Route path="new" element={<WarrantyUpdate />} />
    <Route path=":id">
      <Route index element={<WarrantyDetail />} />
      <Route path="edit" element={<WarrantyUpdate />} />
      <Route path="delete" element={<WarrantyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WarrantyRoutes;
