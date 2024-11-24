import product from 'app/entities/product/product.reducer';
import variant from 'app/entities/variant/variant.reducer';
import color from 'app/entities/color/color.reducer';
import processor from 'app/entities/processor/processor.reducer';
import memory from 'app/entities/memory/memory.reducer';
import storage from 'app/entities/storage/storage.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  product,
  variant,
  color,
  processor,
  memory,
  storage,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
