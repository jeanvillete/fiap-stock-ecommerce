FROM nginx

COPY ./default.conf.template /etc/nginx/conf.d/default.conf.template
COPY ./build /usr/share/nginx/html
COPY ./nginx-env-vars.sh /

ENTRYPOINT [ "/nginx-env-vars.sh" ]

CMD [ "nginx", "-g", "daemon off;" ]