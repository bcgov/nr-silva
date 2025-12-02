# PostGIS Playground

## Using `oc rsh` to reach Ubuntu + PostGIS

1. Log in on OpenShift using the one copied from https://console.apps.silver.devops.gov.bc.ca/
   Select the project `d41ea0-test` using `oc project d41ea0-test`

2. Accessing shells

    - Ubuntu helper container

       ```bash
       oc rsh deploy/ubuntu-postgis17 -c ubuntu-shell
       ```

    - ora2pg container

       ```bash
       oc rsh --container=ora2pg pod/$(oc get pods -l app=ubuntu-postgis17 -o jsonpath='{.items[?(@.status.phase=="Running")].metadata.name}' | awk 'NR==1{print}')
       ```

3. Once inside, use the bundled `psql` to talk to the local PostGIS service:
   ```bash
   psql -h localhost -U postgres -d postgres
   ```
   The helper container mounts the PostGIS PVC at `/var/lib/postgresql`, so you can also inspect the volume with `du`/`df` or touch files directly.

## Files in this folder
- `openshift-postgis-playground.yml` – Deployment that runs both the PostGIS container and the Ubuntu helper shell.
- `Dockerfile` – Builds the Debian/Ubuntu-based helper image with `psql` preinstalled.
- `class-specific manifests` (e.g., `pvc.yml`, `up.yml`) – Storage and other OpenShift resources referenced by the deployment.
- This `README.md` – Overview of the workflow.

## Build process
1. Adjust the Dockerfile to install `postgresql-client` and clean up apt lists:
   ```dockerfile
   FROM ubuntu:24.04
   RUN apt-get update && \
       apt-get install -y --no-install-recommends postgresql-client && \
       rm -rf /var/lib/apt/lists/*
   ```
2. From the OpenShift CLI (inside the project namespace) create/prepare the build:
   ```bash
   oc new-build --name=ubuntu-shell --binary --strategy=docker
   oc start-build ubuntu-shell --from-file=Dockerfile --follow
   ```
   Rename the file or pass `--dockerfile` if the name differs.

## OpenShift commands order
1. Apply PVC/storage manifests (e.g., `oc apply -f pvc.yml`).
2. Start the binary build and wait for completion.
3. Apply the deployment manifest so it pulls the newly built `ubuntu-shell:latest` ImageStreamTag:
   ```bash
   oc apply -f openshift-postgis-playground.yml
   ```
4. Use `oc rsh` to enter the helper container and run `psql` against the PostGIS sidecar:
   ```bash
   oc rsh deploy/ubuntu-postgis17 -c ubuntu-shell -- psql -h localhost -U postgres -d postgres
   ```

## When pods are already running
- To update the helper image, restart the build and then `oc rollout restart deploy/ubuntu-postgis17`.
- If the PostGIS pod is attached to the PVC, check for attach/volume events; the pvc is shared via `/var/lib/postgresql` so you can inspect with `du`/`df` inside the helper container.
- Retrieve credentials programmatically when needed:
  ```bash
  PASSWORD=$(oc get secret postgis17-secret -o jsonpath='{.data.postgres-password}' | base64 -d)
  ```
  and pass `PGPASSWORD` to `psql` to avoid interactive prompts.

Use `oc get pods`, `oc describe pod`, or `oc logs` to troubleshoot running pods before redeploying.
