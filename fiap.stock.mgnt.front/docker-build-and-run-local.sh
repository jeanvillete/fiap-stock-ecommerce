#!/bin/bash

# Este script tem por objetivo possibilitar execução do projeto FE em questão via docker/container NGINX, sem ter
# que de fato construir uma imagem do container para então executar o FE.

npm i
npm run build

export FIAP_SAMPLE_LOGIN_BACKEND_HOST='localhost'
export FIAP_SAMPLE_LOGIN_BACKEND_PORT=8181
export FIAP_STOCK_MGNT_BACKEND_HOST='localhost'
export FIAP_STOCK_MGNT_BACKEND_PORT=8282

envsubst '${FIAP_SAMPLE_LOGIN_BACKEND_HOST} ${FIAP_SAMPLE_LOGIN_BACKEND_PORT} ${FIAP_STOCK_MGNT_BACKEND_HOST} ${FIAP_STOCK_MGNT_BACKEND_PORT}' < default.conf.template > default.conf

docker run \
    --name fiap.stock.mgnt.front-LOCAL \
    --network=host \
    -p 3232:3232 \
    -v "$(pwd)/build/:/usr/share/nginx/html" \
    -v "$(pwd)/default.conf:/etc/nginx/conf.d/default.conf" \
    --rm \
    nginx