import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getColors } from 'app/entities/color/color.reducer';
import { getEntities as getProcessors } from 'app/entities/processor/processor.reducer';
import { getEntities as getMemories } from 'app/entities/memory/memory.reducer';
import { getEntities as getStorages } from 'app/entities/storage/storage.reducer';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { createEntity, getEntity, reset, updateEntity } from './variant.reducer';

export const VariantUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const colors = useAppSelector(state => state.color.entities);
  const processors = useAppSelector(state => state.processor.entities);
  const memories = useAppSelector(state => state.memory.entities);
  const storages = useAppSelector(state => state.storage.entities);
  const products = useAppSelector(state => state.product.entities);
  const variantEntity = useAppSelector(state => state.variant.entity);
  const loading = useAppSelector(state => state.variant.loading);
  const updating = useAppSelector(state => state.variant.updating);
  const updateSuccess = useAppSelector(state => state.variant.updateSuccess);

  const handleClose = () => {
    navigate(`/variant${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getColors({}));
    dispatch(getProcessors({}));
    dispatch(getMemories({}));
    dispatch(getStorages({}));
    dispatch(getProducts({}));
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
      ...variantEntity,
      ...values,
      color: colors.find(it => it.id.toString() === values.color?.toString()),
      processor: processors.find(it => it.id.toString() === values.processor?.toString()),
      memory: memories.find(it => it.id.toString() === values.memory?.toString()),
      storage: storages.find(it => it.id.toString() === values.storage?.toString()),
      products: mapIdList(values.products),
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
          ...variantEntity,
          color: variantEntity?.color?.id,
          processor: variantEntity?.processor?.id,
          memory: variantEntity?.memory?.id,
          storage: variantEntity?.storage?.id,
          products: variantEntity?.products?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissShopAssistantApp.variant.home.createOrEditLabel" data-cy="VariantCreateUpdateHeading">
            <Translate contentKey="larissShopAssistantApp.variant.home.createOrEditLabel">Create or edit a Variant</Translate>
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
                  id="variant-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissShopAssistantApp.variant.label')}
                id="variant-label"
                name="label"
                data-cy="label"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.variant.sku')}
                id="variant-sku"
                name="sku"
                data-cy="sku"
                type="text"
              />
              <ValidatedField
                id="variant-color"
                name="color"
                data-cy="color"
                label={translate('larissShopAssistantApp.variant.color')}
                type="select"
              >
                <option value="" key="0" />
                {colors
                  ? colors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.optionLabel}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="variant-processor"
                name="processor"
                data-cy="processor"
                label={translate('larissShopAssistantApp.variant.processor')}
                type="select"
              >
                <option value="" key="0" />
                {processors
                  ? processors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.optionLabel}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="variant-memory"
                name="memory"
                data-cy="memory"
                label={translate('larissShopAssistantApp.variant.memory')}
                type="select"
              >
                <option value="" key="0" />
                {memories
                  ? memories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.optionLabel}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="variant-storage"
                name="storage"
                data-cy="storage"
                label={translate('larissShopAssistantApp.variant.storage')}
                type="select"
              >
                <option value="" key="0" />
                {storages
                  ? storages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.optionLabel}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.variant.product')}
                id="variant-product"
                data-cy="product"
                type="select"
                multiple
                name="products"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/variant" replace color="info">
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

export default VariantUpdate;
