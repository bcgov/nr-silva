## Use this section once builds are fixed

FROM node:20.19.2-bullseye-slim@sha256:105d30ef2a69e1ca4680b52e9b1b6d871a3c4fb6751539fd6a2a0963629c1fc0 AS build

# Build static files
WORKDIR /app
COPY . .

RUN npm ci --ignore-scripts && \
    npm run build && \
    rm -rf node_modules

# Caddy
FROM caddy:2.10.0-alpine@sha256:e2e3a089760c453bc51c4e718342bd7032d6714f15b437db7121bfc2de2654a6
ENV LOG_LEVEL=info

# Copy static files and config
COPY --from=build /app/build/ /srv
COPY Caddyfile /etc/caddy/Caddyfile

# CA certs and Caddy format
RUN apk add --no-cache ca-certificates && \
    caddy fmt --overwrite /etc/caddy/Caddyfile

# User, port and healthcheck
USER 1001
EXPOSE 3000
HEALTHCHECK CMD curl -f http://localhost/:3000
