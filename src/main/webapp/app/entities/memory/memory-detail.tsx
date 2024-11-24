import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './memory.reducer';

export const MemoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const memoryEntity = useAppSelector(state => state.memory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memoryDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.memory.detail.title">Memory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.id}</dd>
          <dt>
            <span id="attributeLabel">
              <Translate contentKey="larissShopAssistantApp.memory.attributeLabel">Attribute Label</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.attributeLabel}</dd>
          <dt>
            <span id="attributeName">
              <Translate contentKey="larissShopAssistantApp.memory.attributeName">Attribute Name</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.attributeName}</dd>
          <dt>
            <span id="optionLabel">
              <Translate contentKey="larissShopAssistantApp.memory.optionLabel">Option Label</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.optionLabel}</dd>
          <dt>
            <span id="optionValue">
              <Translate contentKey="larissShopAssistantApp.memory.optionValue">Option Value</Translate>
            </span>
          </dt>
          <dd>{memoryEntity.optionValue}</dd>
        </dl>
        <Button tag={Link} to="/memory" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/memory/${memoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemoryDetail;
