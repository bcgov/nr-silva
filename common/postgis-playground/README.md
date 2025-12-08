# PostGIS Playground

## Using `oc rsh` to reach Ubuntu + PostGIS

1. Log in on OpenShift using the one copied from https://console.apps.silver.devops.gov.bc.ca/
   Select the project `d41ea0-test` using `oc project d41ea0-test`

2. Accessing shell
    - ora2pg container

       ```bash
       oc rsh --container=ora2pg pod/$(oc get pods -l app=silva-postgis-playground -o jsonpath='{.items[?(@.status.phase=="Running")].metadata.name}' | awk 'NR==1{print}')
       ```

3. Once inside, use the bundled `psql` to talk to the local PostGIS service:
   ```bash
   psql -h localhost -U postgres -d postgres
   ```
   The helper containers mount the PostGIS PVC at `/silva`, so you can also inspect the volume with `du`/`df` or touch files directly without interfering with PostgreSQL's own data directory.

## Files in this folder
- `deployment.yml` – Deployment that runs both the PostGIS container and the ora2pg container.
- `pvc.yml` - Config to deploy the pvc
