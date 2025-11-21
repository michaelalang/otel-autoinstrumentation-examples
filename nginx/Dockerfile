FROM registry.redhat.io/ubi9/nginx-124 as base
USER 0

RUN dnf -y install \
      libxcrypt-compat libnsl2 ; \
      ln -s /usr/lib64/libnsl.so.3 /usr/lib64/libnsl.so.1 ; \
      yum -y clean all --enablerepo='*'

COPY otel-etc-include.conf /opt/app-root/etc/nginx.default.d/otel-etc-include.conf

CMD nginx -g "daemon off; error_log /dev/stdout info;"
