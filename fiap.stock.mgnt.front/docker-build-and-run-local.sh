#!/bin/bash

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