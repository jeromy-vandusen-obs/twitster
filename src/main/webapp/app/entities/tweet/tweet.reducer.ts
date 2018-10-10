import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITweet, defaultValue } from 'app/shared/model/tweet.model';

export const ACTION_TYPES = {
  FETCH_TWEET_LIST: 'tweet/FETCH_TWEET_LIST',
  FETCH_TWEET: 'tweet/FETCH_TWEET',
  CREATE_TWEET: 'tweet/CREATE_TWEET',
  UPDATE_TWEET: 'tweet/UPDATE_TWEET',
  DELETE_TWEET: 'tweet/DELETE_TWEET',
  RESET: 'tweet/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITweet>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TweetState = Readonly<typeof initialState>;

// Reducer

export default (state: TweetState = initialState, action): TweetState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TWEET_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TWEET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TWEET):
    case REQUEST(ACTION_TYPES.UPDATE_TWEET):
    case REQUEST(ACTION_TYPES.DELETE_TWEET):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TWEET_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TWEET):
    case FAILURE(ACTION_TYPES.CREATE_TWEET):
    case FAILURE(ACTION_TYPES.UPDATE_TWEET):
    case FAILURE(ACTION_TYPES.DELETE_TWEET):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TWEET_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TWEET):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TWEET):
    case SUCCESS(ACTION_TYPES.UPDATE_TWEET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TWEET):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/tweets';

// Actions

export const getEntities: ICrudGetAllAction<ITweet> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TWEET_LIST,
    payload: axios.get<ITweet>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITweet> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TWEET,
    payload: axios.get<ITweet>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITweet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TWEET,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITweet> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TWEET,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITweet> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TWEET,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
