import React from 'react'
import './App.css';
import { Route, Switch, withRouter } from 'react-router-dom'
import Login from './login/Login'
import Menu from './menu/Menu'
import Orders from './orders/Orders'
import Products from './products/Products'
import Addresses from './addresses/Addresses'

function App( { location } ) {
  return (
    <main role="main" className="container">
      <Menu />

      <Switch location={location}>
        <Route exact path='/' component={Login} />
        <Route path='/orders' component={Orders} />
        <Route path='/products' component={Products} />
        <Route path='/addresses' component={Addresses} />
      </Switch>
    </main>
  );
}

export default withRouter(App);