FROM node:16-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm ci --include=dev
COPY . .
ENV NODE_ENV=production
ENV VUE_APP_API_URL=/api
RUN npm run build

FROM nginx:stable-alpine AS production-stage

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup


RUN mkdir -p /var/cache/nginx/client_temp \
    /var/cache/nginx/proxy_temp \
    /var/cache/nginx/fastcgi_temp \
    /var/cache/nginx/uwsgi_temp \
    /var/cache/nginx/scgi_temp \
    /var/run/nginx \
    /var/log/nginx && \
    touch /var/run/nginx/nginx.pid && \
    chown -R appuser:appgroup /var/cache/nginx \
    /var/run/nginx \
    /var/log/nginx \
    /usr/share/nginx/html \
    /etc/nginx && \
    chmod -R 755 /usr/share/nginx/html && \
    chmod 644 /etc/nginx/nginx.conf


RUN rm /etc/nginx/conf.d/default.conf


COPY --from=build-stage --chown=appuser:appgroup /app/dist /usr/share/nginx/html/momo-store


COPY --chown=appuser:appgroup nginx-main.conf /etc/nginx/nginx.conf
COPY --chown=appuser:appgroup nginx.conf /etc/nginx/conf.d/default.conf


HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

USER appuser

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
