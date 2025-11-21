FROM quay.io/fedora/fedora:42 as base

RUN dnf -y install --setopt=tsflags=nodocs --setopt=install_weak_deps=0 --nodocs\
      autoconf automake bzip2 gcc-c++ gd-devel gdb git libcurl-devel \
      libpq-devel libxml2-devel libxslt-devel lsof make mariadb-connector-c-devel \
      openssl-devel patch procps-ng npm redhat-rpm-config sqlite-devel unzip wget which zlib-devel \
      python3.13 python3.13-devel; \
      python3.13 -m ensurepip ;\
      dnf -y clean all --enablerepo='*'

FROM base as builder
COPY requirements.txt /tmp/requirements.txt
RUN dnf -y --setopt=install_weak_deps=0 --nodocs --use-host-config \
      --installroot /output \
      install \
      glibc glibc-minimal-langpack libstdc++ \
      bash \
      python3.13 ; \
      dnf -y clean all --enablerepo='*'

RUN pip3.13 install --prefix=/usr --root /output -r /tmp/requirements.txt

FROM scratch

COPY --from=builder /output / 
COPY app.py /opt/app/app.py
COPY gunicorn_worker.py /opt/app/gunicorn_worker.py

USER 1001
WORKDIR /opt/app
ENTRYPOINT [ "/usr/bin/gunicorn" ]
CMD [ "--pythonpath", "/usr/bin/python3.13", "--bind", "0.0.0.0:8080", "app:app_factory", "--worker-class", "aiohttp.GunicornWebWorker", "--access-logfile", "-", "-c", "/opt/app/gunicorn_worker.py"]
