#!/bin/bash

# publish frontend
docker tag fiap.stock.mgnt.front jvillete/fiap.stock.mgnt.front
docker push jvillete/fiap.stock.mgnt.front

docker tag fiap.stock.portal.front jvillete/fiap.stock.portal.front
docker push jvillete/fiap.stock.portal.front

# publish backend
docker tag fiap.sample.login jvillete/fiap.sample.login
docker push jvillete/fiap.sample.login

docker tag fiap.stock.mgnt jvillete/fiap.stock.mgnt
docker push jvillete/fiap.stock.mgnt

docker tag fiap.stock.portal jvillete/fiap.stock.portal
docker push jvillete/fiap.stock.portal
