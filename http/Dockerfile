FROM registry.redhat.io/ubi10/httpd-24 as base
USER 0

RUN dnf -y install \
      libxcrypt-compat libnsl ; \
      yum -y clean all --enablerepo='*'
