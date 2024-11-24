import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/variant">
        <Translate contentKey="global.menu.entities.variant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/color">
        <Translate contentKey="global.menu.entities.color" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/processor">
        <Translate contentKey="global.menu.entities.processor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/memory">
        <Translate contentKey="global.menu.entities.memory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage">
        <Translate contentKey="global.menu.entities.storage" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
