import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { Color } from 'app/shared/model/enumerations/color.model';
import { Processor } from 'app/shared/model/enumerations/processor.model';
import { Memory } from 'app/shared/model/enumerations/memory.model';
import { Storage } from 'app/shared/model/enumerations/storage.model';
import { createEntity, getEntity, reset, updateEntity } from './product.reducer';

export const ProductUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categories = useAppSelector(state => state.category.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);
  const colorValues = Object.keys(Color);
  const processorValues = Object.keys(Processor);
  const memoryValues = Object.keys(Memory);
  const storageValues = Object.keys(Storage);

  const handleClose = () => {
    navigate(`/product${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategories({}));
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...productEntity,
      ...values,
      category: categories.find(it => it.id.toString() === values.category?.toString()),
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
          color: 'MIDNIGHT',
          processor: 'APPLE_M2_CHIP',
          memory: 'GB_16',
          storage: 'GB_256',
          ...productEntity,
          category: productEntity?.category?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="larissShopAssistantApp.product.home.createOrEditLabel" data-cy="ProductCreateUpdateHeading">
            <Translate contentKey="larissShopAssistantApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
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
                  id="product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('larissShopAssistantApp.product.name')}
                id="product-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.sku')}
                id="product-sku"
                name="sku"
                data-cy="sku"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.price')}
                id="product-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.currency')}
                id="product-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.color')}
                id="product-color"
                name="color"
                data-cy="color"
                type="select"
              >
                {colorValues.map(color => (
                  <option value={color} key={color}>
                    {translate(`larissShopAssistantApp.Color.${color}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.product.processor')}
                id="product-processor"
                name="processor"
                data-cy="processor"
                type="select"
              >
                {processorValues.map(processor => (
                  <option value={processor} key={processor}>
                    {translate(`larissShopAssistantApp.Processor.${processor}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.product.memory')}
                id="product-memory"
                name="memory"
                data-cy="memory"
                type="select"
              >
                {memoryValues.map(memory => (
                  <option value={memory} key={memory}>
                    {translate(`larissShopAssistantApp.Memory.${memory}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.product.storage')}
                id="product-storage"
                name="storage"
                data-cy="storage"
                type="select"
              >
                {storageValues.map(storage => (
                  <option value={storage} key={storage}>
                    {translate(`larissShopAssistantApp.Storage.${storage}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.product.description')}
                id="product-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.feature')}
                id="product-feature"
                name="feature"
                data-cy="feature"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.boxContents')}
                id="product-boxContents"
                name="boxContents"
                data-cy="boxContents"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.warranty')}
                id="product-warranty"
                name="warranty"
                data-cy="warranty"
                type="text"
              />
              <ValidatedField
                id="product-category"
                name="category"
                data-cy="category"
                label={translate('larissShopAssistantApp.product.category')}
                type="select"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product" replace color="info">
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

export default ProductUpdate;
