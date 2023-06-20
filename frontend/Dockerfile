# Build
FROM node:16.19.1-alpine3.17 AS build

# Copy and build
WORKDIR /app
COPY . .
RUN apk add --no-cache python3 g++ make
RUN npm install


# User and startup
EXPOSE 3000
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost/:3000

#CMD
CMD ["npm", "start"]