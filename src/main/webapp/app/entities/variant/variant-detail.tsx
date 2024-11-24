import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './variant.reducer';

export const VariantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const variantEntity = useAppSelector(state => state.variant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="variantDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.variant.detail.title">Variant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{variantEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="larissShopAssistantApp.variant.label">Label</Translate>
            </span>
          </dt>
          <dd>{variantEntity.label}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="larissShopAssistantApp.variant.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{variantEntity.sku}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.variant.color">Color</Translate>
          </dt>
          <dd>{variantEntity.color ? variantEntity.color.optionLabel : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.variant.processor">Processor</Translate>
          </dt>
          <dd>{variantEntity.processor ? variantEntity.processor.optionLabel : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.variant.memory">Memory</Translate>
          </dt>
          <dd>{variantEntity.memory ? variantEntity.memory.optionLabel : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.variant.storage">Storage</Translate>
          </dt>
          <dd>{variantEntity.storage ? variantEntity.storage.optionLabel : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.variant.product">Product</Translate>
          </dt>
          <dd>
            {variantEntity.products
              ? variantEntity.products.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {variantEntity.products && i === variantEntity.products.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/variant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/variant/${variantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VariantDetail;
