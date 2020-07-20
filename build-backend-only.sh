#!/bin/bash

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
