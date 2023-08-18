# Build
FROM node:16.19.1-alpine3.17 AS build

# Dependencies and non-root user
RUN apk add --no-cache python3 g++ make && \
    adduser -S app

# Copy and build
WORKDIR /app
COPY . .
RUN npm install && \
    chown -R app .

# Port and health check
EXPOSE 3000
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost/:3000

# Startup
USER app
CMD ["npm", "start"]
