#!/bin/bash

# Este script tem por objetivo possibilitar execução do projeto FE em questão via docker/container NGINX, sem ter
# que de fato construir uma imagem do container para então executar o FE.

nginxTargetDefaultConf="/tmp/fiap.stock.portal.front.nginx.default.conf"

npm i
npm run build

export FIAP_SAMPLE_LOGIN_BACKEND_HOST='localhost'
export FIAP_SAMPLE_LOGIN_BACKEND_PORT=8181
export FIAP_STOCK_PORTAL_BACKEND_HOST='localhost'
export FIAP_STOCK_PORTAL_BACKEND_PORT=8383

envsubst '${FIAP_SAMPLE_LOGIN_BACKEND_HOST} ${FIAP_SAMPLE_LOGIN_BACKEND_PORT} ${FIAP_STOCK_PORTAL_BACKEND_HOST} ${FIAP_STOCK_PORTAL_BACKEND_PORT}' < default.conf.template > "$nginxTargetDefaultConf"

docker run \
    --name fiap.stock.portal.front-LOCAL \
    --network=host \
    -p 3131:3131 \
    -v "$(pwd)/build/:/usr/share/nginx/html" \
    -v "$nginxTargetDefaultConf:/etc/nginx/conf.d/default.conf" \
    --rm \
    nginx