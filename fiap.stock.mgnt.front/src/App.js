import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Route, Switch, withRouter } from 'react-router-dom'
import Login from './login/Login';

function App() {
  return (
    <Switch>
      <Route path='/'>
        <Login />
      </Route>
    </Switch>
  );
}

export default App;
