FROM ubuntu:24.04

# Non-interactive apt
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y --no-install-recommends gnupg wget lsb-release ca-certificates dirmngr procps locales && \
    echo "deb [signed-by=/usr/share/keyrings/postgresql.gpg] http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list && \
    wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | gpg --dearmor -o /usr/share/keyrings/postgresql.gpg && \
    chmod 644 /usr/share/keyrings/postgresql.gpg && \
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

# Simple healthcheck: verify postgres is accepting connections.
# Increase start-period so initdb and DB startup have time to complete on first run.
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=3 \
    CMD sh -c '\
        PGUSER=${POSTGRES_USER:-postgres}; \
        /usr/lib/postgresql/17/bin/pg_isready -U "$PGUSER" -h 127.0.0.1 -p 5432 || exit 1'

# Default command: start postgres (ensures entrypoint init path runs)
CMD ["postgres"]
