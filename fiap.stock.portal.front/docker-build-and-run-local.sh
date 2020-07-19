#!/bin/bash

npm i
npm run build
docker run \
    --name fiap.stock.portal.front-LOCAL \
    --network=host \
    -p 3131:3131 \
    -v "$(pwd)/build/:/usr/share/nginx/html" \
    -v "$(pwd)/default.conf:/etc/nginx/conf.d/default.conf" \
    --rm \
    nginx