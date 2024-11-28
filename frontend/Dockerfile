## Use this section once builds are fixed

FROM node:20.18.1-bullseye-slim AS build

# Build static files
WORKDIR /app
COPY . .

RUN npm ci --ignore-scripts && \
    npm run build && \
    rm -rf node_modules

# Caddy
FROM caddy:2.8.4-alpine
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
