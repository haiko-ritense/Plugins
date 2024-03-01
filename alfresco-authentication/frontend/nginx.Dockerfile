FROM nginx:1.21.1-alpine

COPY ./deployment/dist/ /usr/share/nginx/html

# When the container starts, replace the env.js with values from environment variables
CMD ["/bin/sh",  "-c",  "envsubst < /usr/share/nginx/html/assets/config.template.js > /usr/share/nginx/html/assets/config.js && exec nginx -g 'daemon off;'"]

EXPOSE 80
