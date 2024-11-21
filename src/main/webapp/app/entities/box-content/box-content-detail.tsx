import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './box-content.reducer';

export const BoxContentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const boxContentEntity = useAppSelector(state => state.boxContent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="boxContentDetailsHeading">
          <Translate contentKey="larissShopAssistantApp.boxContent.detail.title">BoxContent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{boxContentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissShopAssistantApp.boxContent.name">Name</Translate>
            </span>
          </dt>
          <dd>{boxContentEntity.name}</dd>
          <dt>
            <span id="contentEn">
              <Translate contentKey="larissShopAssistantApp.boxContent.contentEn">Content En</Translate>
            </span>
          </dt>
          <dd>{boxContentEntity.contentEn}</dd>
          <dt>
            <span id="contentId">
              <Translate contentKey="larissShopAssistantApp.boxContent.contentId">Content Id</Translate>
            </span>
          </dt>
          <dd>{boxContentEntity.contentId}</dd>
        </dl>
        <Button tag={Link} to="/box-content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/box-content/${boxContentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BoxContentDetail;
