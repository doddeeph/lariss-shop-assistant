import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './processor.reducer';

export const ProcessorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const processorEntity = useAppSelector(state => state.processor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="processorDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.processor.detail.title">Processor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{processorEntity.id}</dd>
          <dt>
            <span id="attributeLabel">
              <Translate contentKey="larissShopAssistantApp.processor.attributeLabel">Attribute Label</Translate>
            </span>
          </dt>
          <dd>{processorEntity.attributeLabel}</dd>
          <dt>
            <span id="attributeName">
              <Translate contentKey="larissShopAssistantApp.processor.attributeName">Attribute Name</Translate>
            </span>
          </dt>
          <dd>{processorEntity.attributeName}</dd>
          <dt>
            <span id="optionLabel">
              <Translate contentKey="larissShopAssistantApp.processor.optionLabel">Option Label</Translate>
            </span>
          </dt>
          <dd>{processorEntity.optionLabel}</dd>
          <dt>
            <span id="optionValue">
              <Translate contentKey="larissShopAssistantApp.processor.optionValue">Option Value</Translate>
            </span>
          </dt>
          <dd>{processorEntity.optionValue}</dd>
        </dl>
        <Button tag={Link} to="/processor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/processor/${processorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProcessorDetail;
