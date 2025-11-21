# OpenTelemetery Autoinstrumentation workshop

The following examples will show howto use OpenTelemetry's autoinstrumentation features in combination with OpenTelemetry Collector to instrument some custom Applications and forward logs/traces/metrics to a centralized collector or Signal storage system (like Tempo, Loki Grafana)


## Base namespace

The basic setup requires the namespace, OpenTelemetryCollector and OpenTelemetry AutoInstrumentation CR to be deployed.
These components are shared between all auto instrumented deployments and still provide tenancy as described below.
Execute the following command to create them

```
oc create -k deploy
```


## Example applications

The repository includes 5 example applications:
* python3.12
* python3.13
* go binary
* Apache httpd
* Nginx 

### Building the images
All applications require you to build your own image for using it. The included Dockerfiles are based upon upstream and free to use image sources except Apache and Nginx which are Red Hat registry sources as there are no free ubi9/10 release versions of those images around.

#### python3.12 and python3.13 images
based on Fedora 42, python3.12 and python3.13 images build with a simple 
```
podman build -f Dockerfile 
```
in their respective directories. Tagged and push to your Registry repository they can be used after a few moments on building them from the source.

#### go binary image 
the go binary image is based on Fedora 42 as well and requires you to first do 
```
go mod tidy
go build main.go
```
commands to create the binary main which is than included into the Fedora base image for execution and instrumentation with the known
```
podman build -f Dockerfile
``` 
in the go sub directory.

#### Apache 2.4 httpd image
the Apache image is based on the Red Hat UBI10 version of Apache 2.4 and requires one to include the libraries:
* libxcrypt-compat 
* libnsl

for the OpenTelemetry autoinstrumentation to work
in the http directory executing the 
```
podman build -f Dockerfile
```
will create the image if you do have access to registry.redhat.io. Until it's released on quay.io or docker it's not expected to build without the mentioned registry credentials.

#### Nginx 1.24.0 image 
the Nginx image is based on Red Hat UBI9 version and requires one to include the libraries:
* libxcrypt-compat 
* libnsl2

furthermore since UBI9 only provides libnsl2 which ships the libnsl.so.3 version, a symlink to the respective libnsl.so.1 needs to be created.

for the OpenTelemetry autoinstrumentation to work
in the nginx directory executing the
```
podman build -f Dockerfile
```
will create the image if you do have access to registry.redhat.io. Until it's released on quay.io or docker it's not expected to build without the mentioned registry credentials.

## Deploying the sample applications

To deploy any variant into an existing OpenShift Cluster which does have the OpenTelemetryCollector installed execute the following command
```
oc apply -k python3.12/deploy
```

**NOTE** to ensure when switching from application a to b that all injectors and configurations are in place ensure to use **replace** and not apply.
```
oc replace -k http/deploy
```

### Telemetry data

Out-of-the-box, the deployent only show's various signals from the autoinstrumentation on the `otc-container` container logs through stdout (exporter debug).

Further in the configuration, the option to add a centralized collector or Signal Store (Tempo/Loki/...) can be enabled in the Pipelines accordingly. 

For multi-tenancy, the instrumentation and OpenTelemetryCollector are configured to set the resource Attribute `tenant` with example values (tenantA, tenantB). The annotation on the deployment can subscript to each individual instrumentation to even reach tenancy within one namespace.

