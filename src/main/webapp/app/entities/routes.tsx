import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Product from './product';
import Variant from './variant';
import Color from './color';
import Processor from './processor';
import Memory from './memory';
import Storage from './storage';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="product/*" element={<Product />} />
        <Route path="variant/*" element={<Variant />} />
        <Route path="color/*" element={<Color />} />
        <Route path="processor/*" element={<Processor />} />
        <Route path="memory/*" element={<Memory />} />
        <Route path="storage/*" element={<Storage />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
