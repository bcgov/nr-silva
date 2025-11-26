FROM ubuntu:24.04

# Non-interactive apt
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends gnupg wget lsb-release ca-certificates dirmngr procps locales && \
    echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor -o /usr/share/keyrings/postgresql.gpg && \
    apt-get update && \
    apt-get install -y --no-install-recommends postgresql-17 postgresql-17-postgis-3 postgis && \
    rm -rf /var/lib/apt/lists/*

# Create postgres user data dir and set locale
RUN mkdir -p /var/lib/postgresql/data && chown -R postgres:postgres /var/lib/postgresql

# Copy entrypoint
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

USER postgres

EXPOSE 5432

VOLUME ["/var/lib/postgresql/data"]

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
