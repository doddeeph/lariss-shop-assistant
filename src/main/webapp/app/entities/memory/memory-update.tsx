import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './memory.reducer';

export const MemoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const memoryEntity = useAppSelector(state => state.memory.entity);
  const loading = useAppSelector(state => state.memory.loading);
  const updating = useAppSelector(state => state.memory.updating);
  const updateSuccess = useAppSelector(state => state.memory.updateSuccess);

  const handleClose = () => {
    navigate(`/memory${location.search}`);
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
      ...memoryEntity,
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
          ...memoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissShopAssistantApp.memory.home.createOrEditLabel" data-cy="MemoryCreateUpdateHeading">
            <Translate contentKey="larissShopAssistantApp.memory.home.createOrEditLabel">Create or edit a Memory</Translate>
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
                  id="memory-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissShopAssistantApp.memory.attributeLabel')}
                id="memory-attributeLabel"
                name="attributeLabel"
                data-cy="attributeLabel"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.memory.attributeName')}
                id="memory-attributeName"
                name="attributeName"
                data-cy="attributeName"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.memory.optionLabel')}
                id="memory-optionLabel"
                name="optionLabel"
                data-cy="optionLabel"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.memory.optionValue')}
                id="memory-optionValue"
                name="optionValue"
                data-cy="optionValue"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/memory" replace color="info">
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

export default MemoryUpdate;
