#!/bin/bash

# build FE
pushd fiap.stock.mgnt.front/
  npm run build
  docker build -t fiap.stock.mgnt.front .
popd

pushd fiap.stock.portal.front/
  npm run build
  docker build -t fiap.stock.portal.front .
popd

# build BE
pushd fiap.sample.login/
  mvn clean package
  docker build -t fiap.sample.login .
popd

pushd fiap.stock.mgnt/
  mvn clean package
  docker build -t fiap.stock.mgnt .
popd

pushd fiap.stock.portal/
  mvn clean package
  docker build -t fiap.stock.portal .
popd
