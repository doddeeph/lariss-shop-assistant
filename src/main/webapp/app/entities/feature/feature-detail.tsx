import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feature.reducer';

export const FeatureDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const featureEntity = useAppSelector(state => state.feature.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="featureDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.feature.detail.title">Feature</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{featureEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissShopAssistantApp.feature.name">Name</Translate>
            </span>
          </dt>
          <dd>{featureEntity.name}</dd>
          <dt>
            <span id="featureEn">
              <Translate contentKey="larissShopAssistantApp.feature.featureEn">Feature En</Translate>
            </span>
          </dt>
          <dd>{featureEntity.featureEn}</dd>
          <dt>
            <span id="featureId">
              <Translate contentKey="larissShopAssistantApp.feature.featureId">Feature Id</Translate>
            </span>
          </dt>
          <dd>{featureEntity.featureId}</dd>
        </dl>
        <Button tag={Link} to="/feature" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feature/${featureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeatureDetail;
