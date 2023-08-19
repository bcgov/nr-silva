FROM node:16.19.1-alpine3.17 AS build

# Build
RUN apk add --no-cache python3 g++ make && \
    npm ci && \
    npm run build && \
    npm rm -rf node_modules

# Caddy
FROM caddy:2.7.3-alpine

# Copy static files and config
COPY --from=build /app/build/ /app/dist
COPY --from=build /app/Caddyfile /etc/caddy/Caddyfile

# Caddy format
RUN caddy fmt --overwrite /etc/caddy/Caddyfile

# User, port and healthcheck
USER 1001
EXPOSE 3000
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:3000
