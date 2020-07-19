#!/bin/bash

# Este script tem por objetivo possibilitar execução do projeto FE em questão via docker/container NGINX, sem ter
# que de fato construir uma imagem do container para então executar o FE.

npm i
npm run build
docker run \
    --name fiap.stock.mgnt.front-LOCAL \
    --network=host \
    -p 3232:3232 \
    -v "$(pwd)/build/:/usr/share/nginx/html" \
    -v "$(pwd)/default.conf:/etc/nginx/conf.d/default.conf" \
    --rm \
    nginx