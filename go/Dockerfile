FROM quay.io/fedora/fedora:42 as base

RUN dnf -y --setopt=install_weak_deps=0 --nodocs --use-host-config \
      --installroot /output \
      install \
      glibc glibc-minimal-langpack libstdc++ ; \
      dnf -y clean all --enablerepo='*'


FROM scratch

COPY --from=base /output /
COPY main /main

USER 1001
WORKDIR /
ENTRYPOINT [ "/main" ]
