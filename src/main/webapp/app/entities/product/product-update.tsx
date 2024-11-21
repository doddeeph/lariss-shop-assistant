import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntities as getDescriptions } from 'app/entities/description/description.reducer';
import { getEntities as getFeatures } from 'app/entities/feature/feature.reducer';
import { getEntities as getBoxContents } from 'app/entities/box-content/box-content.reducer';
import { getEntities as getWarranties } from 'app/entities/warranty/warranty.reducer';
import { DiscountType } from 'app/shared/model/enumerations/discount-type.model';
import { Currency } from 'app/shared/model/enumerations/currency.model';
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
  const descriptions = useAppSelector(state => state.description.entities);
  const features = useAppSelector(state => state.feature.entities);
  const boxContents = useAppSelector(state => state.boxContent.entities);
  const warranties = useAppSelector(state => state.warranty.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);
  const discountTypeValues = Object.keys(DiscountType);
  const currencyValues = Object.keys(Currency);
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
    dispatch(getDescriptions({}));
    dispatch(getFeatures({}));
    dispatch(getBoxContents({}));
    dispatch(getWarranties({}));
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
    if (values.basePrice !== undefined && typeof values.basePrice !== 'number') {
      values.basePrice = Number(values.basePrice);
    }
    if (values.discountPrice !== undefined && typeof values.discountPrice !== 'number') {
      values.discountPrice = Number(values.discountPrice);
    }
    if (values.discountAmount !== undefined && typeof values.discountAmount !== 'number') {
      values.discountAmount = Number(values.discountAmount);
    }

    const entity = {
      ...productEntity,
      ...values,
      category: categories.find(it => it.id.toString() === values.category?.toString()),
      description: descriptions.find(it => it.id.toString() === values.description?.toString()),
      feature: features.find(it => it.id.toString() === values.feature?.toString()),
      boxContent: boxContents.find(it => it.id.toString() === values.boxContent?.toString()),
      warranty: warranties.find(it => it.id.toString() === values.warranty?.toString()),
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
          discountType: 'PERCENTAGE',
          currency: 'IDR',
          color: 'MIDNIGHT',
          processor: 'APPLE_M2_8CPU_8GPU',
          memory: 'MEMORY_16GB',
          storage: 'STORAGE_256GB',
          ...productEntity,
          category: productEntity?.category?.id,
          description: productEntity?.description?.id,
          feature: productEntity?.feature?.id,
          boxContent: productEntity?.boxContent?.id,
          warranty: productEntity?.warranty?.id,
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
                label={translate('larissShopAssistantApp.product.basePrice')}
                id="product-basePrice"
                name="basePrice"
                data-cy="basePrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.discountPrice')}
                id="product-discountPrice"
                name="discountPrice"
                data-cy="discountPrice"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.discountAmount')}
                id="product-discountAmount"
                name="discountAmount"
                data-cy="discountAmount"
                type="text"
              />
              <ValidatedField
                label={translate('larissShopAssistantApp.product.discountType')}
                id="product-discountType"
                name="discountType"
                data-cy="discountType"
                type="select"
              >
                {discountTypeValues.map(discountType => (
                  <option value={discountType} key={discountType}>
                    {translate(`larissShopAssistantApp.DiscountType.${discountType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('larissShopAssistantApp.product.currency')}
                id="product-currency"
                name="currency"
                data-cy="currency"
                type="select"
              >
                {currencyValues.map(currency => (
                  <option value={currency} key={currency}>
                    {translate(`larissShopAssistantApp.Currency.${currency}`)}
                  </option>
                ))}
              </ValidatedField>
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
                        {otherEntity.categoryEn}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-description"
                name="description"
                data-cy="description"
                label={translate('larissShopAssistantApp.product.description')}
                type="select"
              >
                <option value="" key="0" />
                {descriptions
                  ? descriptions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-feature"
                name="feature"
                data-cy="feature"
                label={translate('larissShopAssistantApp.product.feature')}
                type="select"
              >
                <option value="" key="0" />
                {features
                  ? features.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-boxContent"
                name="boxContent"
                data-cy="boxContent"
                label={translate('larissShopAssistantApp.product.boxContent')}
                type="select"
              >
                <option value="" key="0" />
                {boxContents
                  ? boxContents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="product-warranty"
                name="warranty"
                data-cy="warranty"
                label={translate('larissShopAssistantApp.product.warranty')}
                type="select"
              >
                <option value="" key="0" />
                {warranties
                  ? warranties.map(otherEntity => (
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
