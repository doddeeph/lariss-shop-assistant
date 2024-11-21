import category from 'app/entities/category/category.reducer';
import product from 'app/entities/product/product.reducer';
import description from 'app/entities/description/description.reducer';
import feature from 'app/entities/feature/feature.reducer';
import boxContent from 'app/entities/box-content/box-content.reducer';
import warranty from 'app/entities/warranty/warranty.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  category,
  product,
  description,
  feature,
  boxContent,
  warranty,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
