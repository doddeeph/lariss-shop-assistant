import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissShopAssistantApp.product.name">Name</Translate>
            </span>
          </dt>
          <dd>{productEntity.name}</dd>
          <dt>
            <span id="sku">
              <Translate contentKey="larissShopAssistantApp.product.sku">Sku</Translate>
            </span>
          </dt>
          <dd>{productEntity.sku}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="larissShopAssistantApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="larissShopAssistantApp.product.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{productEntity.currency}</dd>
          <dt>
            <span id="color">
              <Translate contentKey="larissShopAssistantApp.product.color">Color</Translate>
            </span>
          </dt>
          <dd>{productEntity.color}</dd>
          <dt>
            <span id="processor">
              <Translate contentKey="larissShopAssistantApp.product.processor">Processor</Translate>
            </span>
          </dt>
          <dd>{productEntity.processor}</dd>
          <dt>
            <span id="memory">
              <Translate contentKey="larissShopAssistantApp.product.memory">Memory</Translate>
            </span>
          </dt>
          <dd>{productEntity.memory}</dd>
          <dt>
            <span id="storage">
              <Translate contentKey="larissShopAssistantApp.product.storage">Storage</Translate>
            </span>
          </dt>
          <dd>{productEntity.storage}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="larissShopAssistantApp.product.description">Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="feature">
              <Translate contentKey="larissShopAssistantApp.product.feature">Feature</Translate>
            </span>
          </dt>
          <dd>{productEntity.feature}</dd>
          <dt>
            <span id="boxContents">
              <Translate contentKey="larissShopAssistantApp.product.boxContents">Box Contents</Translate>
            </span>
          </dt>
          <dd>{productEntity.boxContents}</dd>
          <dt>
            <span id="warranty">
              <Translate contentKey="larissShopAssistantApp.product.warranty">Warranty</Translate>
            </span>
          </dt>
          <dd>{productEntity.warranty}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.product.category">Category</Translate>
          </dt>
          <dd>{productEntity.category ? productEntity.category.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;