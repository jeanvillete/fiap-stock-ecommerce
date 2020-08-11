#!/bin/bash

# build FE
pushd fiap.stock.mgnt.front/
  npm i
  npm run build
  docker build -t fiap.stock.mgnt.front .
popd

pushd fiap.stock.portal.front/
  npm i
  npm run build
  docker build -t fiap.stock.portal.front .
popd
