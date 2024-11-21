import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './product.reducer';

export const Product = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productList = useAppSelector(state => state.product.entities);
  const loading = useAppSelector(state => state.product.loading);
  const totalItems = useAppSelector(state => state.product.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="product-heading" data-cy="ProductHeading">
        <Translate contentKey="larissShopAssistantApp.product.home.title">Products</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="larissShopAssistantApp.product.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="larissShopAssistantApp.product.home.createLabel">Create new Product</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {productList && productList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="larissShopAssistantApp.product.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="larissShopAssistantApp.product.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('sku')}>
                  <Translate contentKey="larissShopAssistantApp.product.sku">Sku</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sku')} />
                </th>
                <th className="hand" onClick={sort('basePrice')}>
                  <Translate contentKey="larissShopAssistantApp.product.basePrice">Base Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('basePrice')} />
                </th>
                <th className="hand" onClick={sort('discountPrice')}>
                  <Translate contentKey="larissShopAssistantApp.product.discountPrice">Discount Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountPrice')} />
                </th>
                <th className="hand" onClick={sort('discountAmount')}>
                  <Translate contentKey="larissShopAssistantApp.product.discountAmount">Discount Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountAmount')} />
                </th>
                <th className="hand" onClick={sort('discountType')}>
                  <Translate contentKey="larissShopAssistantApp.product.discountType">Discount Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountType')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="larissShopAssistantApp.product.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('color')}>
                  <Translate contentKey="larissShopAssistantApp.product.color">Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('color')} />
                </th>
                <th className="hand" onClick={sort('processor')}>
                  <Translate contentKey="larissShopAssistantApp.product.processor">Processor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processor')} />
                </th>
                <th className="hand" onClick={sort('memory')}>
                  <Translate contentKey="larissShopAssistantApp.product.memory">Memory</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('memory')} />
                </th>
                <th className="hand" onClick={sort('storage')}>
                  <Translate contentKey="larissShopAssistantApp.product.storage">Storage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storage')} />
                </th>
                <th>
                  <Translate contentKey="larissShopAssistantApp.product.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissShopAssistantApp.product.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissShopAssistantApp.product.feature">Feature</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissShopAssistantApp.product.boxContent">Box Content</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="larissShopAssistantApp.product.warranty">Warranty</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productList.map((product, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product/${product.id}`} color="link" size="sm">
                      {product.id}
                    </Button>
                  </td>
                  <td>{product.name}</td>
                  <td>{product.sku}</td>
                  <td>{product.basePrice}</td>
                  <td>{product.discountPrice}</td>
                  <td>{product.discountAmount}</td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.DiscountType.${product.discountType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.Currency.${product.currency}`} />
                  </td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.Color.${product.color}`} />
                  </td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.Processor.${product.processor}`} />
                  </td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.Memory.${product.memory}`} />
                  </td>
                  <td>
                    <Translate contentKey={`larissShopAssistantApp.Storage.${product.storage}`} />
                  </td>
                  <td>{product.category ? <Link to={`/category/${product.category.id}`}>{product.category.categoryEn}</Link> : ''}</td>
                  <td>
                    {product.description ? <Link to={`/description/${product.description.id}`}>{product.description.name}</Link> : ''}
                  </td>
                  <td>{product.feature ? <Link to={`/feature/${product.feature.id}`}>{product.feature.name}</Link> : ''}</td>
                  <td>{product.boxContent ? <Link to={`/box-content/${product.boxContent.id}`}>{product.boxContent.name}</Link> : ''}</td>
                  <td>{product.warranty ? <Link to={`/warranty/${product.warranty.id}`}>{product.warranty.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product/${product.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/product/${product.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/product/${product.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="larissShopAssistantApp.product.home.notFound">No Products found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={productList && productList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Product;
