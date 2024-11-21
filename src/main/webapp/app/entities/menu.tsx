import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/description">
        <Translate contentKey="global.menu.entities.description" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/feature">
        <Translate contentKey="global.menu.entities.feature" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/box-content">
        <Translate contentKey="global.menu.entities.boxContent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/warranty">
        <Translate contentKey="global.menu.entities.warranty" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
