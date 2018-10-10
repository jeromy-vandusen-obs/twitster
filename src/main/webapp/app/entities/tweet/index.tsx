import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tweet from './tweet';
import TweetDetail from './tweet-detail';
import TweetUpdate from './tweet-update';
import TweetDeleteDialog from './tweet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TweetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TweetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TweetDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tweet} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TweetDeleteDialog} />
  </>
);

export default Routes;
