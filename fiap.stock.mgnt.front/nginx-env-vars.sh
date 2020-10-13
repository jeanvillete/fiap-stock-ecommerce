#!/usr/bin/env sh

[ -n "$FIAP_SAMPLE_LOGIN_BACKEND_HOST" ] || { echo "fiap.stock.mgnt.front; Environment variable not defined; FIAP_SAMPLE_LOGIN_BACKEND_HOST"; exit 1; }
[ -n "$FIAP_SAMPLE_LOGIN_BACKEND_PORT" ] || { echo "fiap.stock.mgnt.front; Environment variable not defined; FIAP_SAMPLE_LOGIN_BACKEND_PORT"; exit 1; }
[ -n "$FIAP_STOCK_MGNT_BACKEND_HOST" ] || { echo "fiap.stock.mgnt.front; Environment variable not defined; FIAP_STOCK_MGNT_BACKEND_HOST"; exit 1; }
[ -n "$FIAP_STOCK_MGNT_BACKEND_PORT" ] || { echo "fiap.stock.mgnt.front; Environment variable not defined; FIAP_STOCK_MGNT_BACKEND_PORT"; exit 1; }

envsubst '${FIAP_SAMPLE_LOGIN_BACKEND_HOST} ${FIAP_SAMPLE_LOGIN_BACKEND_PORT} ${FIAP_STOCK_MGNT_BACKEND_HOST} ${FIAP_STOCK_MGNT_BACKEND_PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

exec "$@"