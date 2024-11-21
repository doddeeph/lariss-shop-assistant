import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './description.reducer';

export const DescriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const descriptionEntity = useAppSelector(state => state.description.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="descriptionDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.description.detail.title">Description</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissShopAssistantApp.description.name">Name</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.name}</dd>
          <dt>
            <span id="descriptionEn">
              <Translate contentKey="larissShopAssistantApp.description.descriptionEn">Description En</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.descriptionEn}</dd>
          <dt>
            <span id="descriptionId">
              <Translate contentKey="larissShopAssistantApp.description.descriptionId">Description Id</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.descriptionId}</dd>
        </dl>
        <Button tag={Link} to="/description" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/description/${descriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DescriptionDetail;
