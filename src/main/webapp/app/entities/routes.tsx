import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Product from './product';
import Description from './description';
import Feature from './feature';
import BoxContent from './box-content';
import Warranty from './warranty';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="description/*" element={<Description />} />
        <Route path="feature/*" element={<Feature />} />
        <Route path="box-content/*" element={<BoxContent />} />
        <Route path="warranty/*" element={<Warranty />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
