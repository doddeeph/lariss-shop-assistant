import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage.reducer';

export const StorageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageEntity = useAppSelector(state => state.storage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.storage.detail.title">Storage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageEntity.id}</dd>
          <dt>
            <span id="attributeLabel">
              <Translate contentKey="larissShopAssistantApp.storage.attributeLabel">Attribute Label</Translate>
            </span>
          </dt>
          <dd>{storageEntity.attributeLabel}</dd>
          <dt>
            <span id="attributeName">
              <Translate contentKey="larissShopAssistantApp.storage.attributeName">Attribute Name</Translate>
            </span>
          </dt>
          <dd>{storageEntity.attributeName}</dd>
          <dt>
            <span id="optionLabel">
              <Translate contentKey="larissShopAssistantApp.storage.optionLabel">Option Label</Translate>
            </span>
          </dt>
          <dd>{storageEntity.optionLabel}</dd>
          <dt>
            <span id="optionValue">
              <Translate contentKey="larissShopAssistantApp.storage.optionValue">Option Value</Translate>
            </span>
          </dt>
          <dd>{storageEntity.optionValue}</dd>
        </dl>
        <Button tag={Link} to="/storage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage/${storageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageDetail;
