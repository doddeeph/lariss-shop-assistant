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
            <span id="basePrice">
              <Translate contentKey="larissShopAssistantApp.product.basePrice">Base Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.basePrice}</dd>
          <dt>
            <span id="discountPrice">
              <Translate contentKey="larissShopAssistantApp.product.discountPrice">Discount Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.discountPrice}</dd>
          <dt>
            <span id="discountAmount">
              <Translate contentKey="larissShopAssistantApp.product.discountAmount">Discount Amount</Translate>
            </span>
          </dt>
          <dd>{productEntity.discountAmount}</dd>
          <dt>
            <span id="discountType">
              <Translate contentKey="larissShopAssistantApp.product.discountType">Discount Type</Translate>
            </span>
          </dt>
          <dd>{productEntity.discountType}</dd>
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
            <Translate contentKey="larissShopAssistantApp.product.category">Category</Translate>
          </dt>
          <dd>{productEntity.category ? productEntity.category.categoryEn : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.product.description">Description</Translate>
          </dt>
          <dd>{productEntity.description ? productEntity.description.name : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.product.feature">Feature</Translate>
          </dt>
          <dd>{productEntity.feature ? productEntity.feature.name : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.product.boxContent">Box Content</Translate>
          </dt>
          <dd>{productEntity.boxContent ? productEntity.boxContent.name : ''}</dd>
          <dt>
            <Translate contentKey="larissShopAssistantApp.product.warranty">Warranty</Translate>
          </dt>
          <dd>{productEntity.warranty ? productEntity.warranty.name : ''}</dd>
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
