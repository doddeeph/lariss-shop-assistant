import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './warranty.reducer';

export const WarrantyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const warrantyEntity = useAppSelector(state => state.warranty.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="warrantyDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.warranty.detail.title">Warranty</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{warrantyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissShopAssistantApp.warranty.name">Name</Translate>
            </span>
          </dt>
          <dd>{warrantyEntity.name}</dd>
          <dt>
            <span id="warrantyEn">
              <Translate contentKey="larissShopAssistantApp.warranty.warrantyEn">Warranty En</Translate>
            </span>
          </dt>
          <dd>{warrantyEntity.warrantyEn}</dd>
          <dt>
            <span id="warrantyId">
              <Translate contentKey="larissShopAssistantApp.warranty.warrantyId">Warranty Id</Translate>
            </span>
          </dt>
          <dd>{warrantyEntity.warrantyId}</dd>
        </dl>
        <Button tag={Link} to="/warranty" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/warranty/${warrantyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WarrantyDetail;
