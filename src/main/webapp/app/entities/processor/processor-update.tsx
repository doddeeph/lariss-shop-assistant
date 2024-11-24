import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './processor.reducer';

export const ProcessorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const processorEntity = useAppSelector(state => state.processor.entity);
  const loading = useAppSelector(state => state.processor.loading);
  const updating = useAppSelector(state => state.processor.updating);
  const updateSuccess = useAppSelector(state => state.processor.updateSuccess);

  const handleClose = () => {
    navigate(`/processor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...processorEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...processorEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissShopAssistantApp.processor.home.createOrEditLabel" data-cy="ProcessorCreateUpdateHeading">
            <Translate contentKey="larissShopAssistantApp.processor.home.createOrEditLabel">Create or edit a Processor</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="processor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissShopAssistantApp.processor.attributeLabel')}
                id="processor-attributeLabel"
                name="attributeLabel"
                data-cy="attributeLabel"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.processor.attributeName')}
                id="processor-attributeName"
                name="attributeName"
                data-cy="attributeName"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.processor.optionLabel')}
                id="processor-optionLabel"
                name="optionLabel"
                data-cy="optionLabel"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.processor.optionValue')}
                id="processor-optionValue"
                name="optionValue"
                data-cy="optionValue"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/processor" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProcessorUpdate;
