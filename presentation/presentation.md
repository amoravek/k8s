# Co to je Docker? [5 min]

# ...je to nástroj, který umožňuje:

- vytvořit artefakt schopný běhu všude tam, kde je Docker
- zapouzdřit aplikaci včetně jejího prostředí
- redukovat problémy při nasazování aplikací
- řešit podobné úlohy jako VM

.notes: zminit napr. komplexitu nasazeni webapp na vice AS, vice OS, apod.

.notes: redukce problemu = redukuje pacive problemy dneska, prinasi ale nove (nova tech.)

#
#

.footer: Základy Docker a Kubernetes 01/2021

---
# Srovnání s VM [5 min]

![docker-vs-vm](docker-vs-vm.png)

.notes: zmínit prakticky okamžité spuštění oproti nutnosti bootu VM

.notes: zmínit řádové snížení velikosti image oproti VM

---
# Linux kernel [5 min]

- Docker je produkt založený na vlastnostech a funkcích jádra **Linuxu**
- nefunguje nativně např. na AIX, Windows ani macOS (jen s virtualizační mezivrstvou nebo s plnohodnotným Linuxem jako součástí OS)
- všechny kontejnery (viz dále) sdílí s host OS jeho linux kernel
- kontejnery nejsou nic jiného než běžné procesy uzavřené do vlastního namespace a se zdroji omezenými za pomocí control groups (obojí funkce linuxového jádra)
- kontejner tedy není žádný "aktivní obal", ale jen logicky izolovaný proces, který se tváří jako samostatná VM

---
# Motivace [5 min]

- distribuce neměnného balíčku (=docker image) k zákazníkovi
- image se nedají měnit, ale může se z nich *dědit*
- docker image obsahuje vše potřebné pro spuštění aplikace
- image lze digitálně podepsat
- součástí image i kompletní souborový systém OS - např. OS CVE lze řešit rebuildem image
- image se verzují

---
# Image [5 min] 

## Co je vlastně image?

- lze si představit jako .tar.gz souborového systému OS + nějaká metadata
- *image* je vždy read-only, vytváří se *buildem*
- image se většinou zakládá na nějakém již existujícím - např. JDK image dědí z Alpine linux image a dodá instalaci JDK
#
    !dockerfile
    FROM alpine
    RUN <instalace jdk>
#
(je to něco jako vzít souborový systém Alpine linuxu, nakopírovat tam JDK, zazipovat a obalit metadaty)

---

# Parent a base image [5 min] 

- parent image referencován instrukcí `FROM`
- parent images dostupné např. na Docker HUB
- kde je ale root image? Je to image s `FROM scratch`, ale častěji se vytváří importem zazipovaného souborového systému:

.notes: zmínit absenci init

.notes: scratch je rezervovaný název - prázdný parent image

#
    !bash
    $ sudo debootstrap xenial xenial > /dev/null
    $ sudo tar -C xenial -c . | docker import - xenial

    a29c15f1bf7a

    $ docker run xenial cat /etc/lsb-release

    DISTRIB_ID=Ubuntu
    DISTRIB_RELEASE=16.04
    DISTRIB_CODENAME=xenial
    DISTRIB_DESCRIPTION="Ubuntu 16.04 LTS"    

---

# Kontejner [5 min] 
- *kontejner* je vlastní aktivní proces spuštěný nad souborovým systémem z image (lze si představit jako *instanci* image)
- spuštění kontejneru s aplikací = spuštění procesu aplikace na systému, kde běží Docker daemon, ale proces je izolován ve vlastním *namespace* a běží jako root proces (PID 1)
- root procesem host OS je zpravidla initd nebo systemd - kontejner init proces nemá, protože nebootuje, začíná přímo spuštěním procesu

---
# Docker daemon, OCI, Docker CLI [15 min] 
---
# Dockerfile, docker kontext, .dockerignore [45 min] 
---
# Přestávka [15 min] 
---
# Dockerfile - příkazy [15 min] 
---
# CMD a ENTRYPOINT - exec vs shell formy, PID 1 [25 min] 
---
# Build, Vrstvy, multi-stage build [30 min] 
---
# Pojmenovávání images, Docker repository [5 min] 
---
# Docker registry - secure/insecure, harbor.trask.cz [5 min] 
---
# pull, push, tag, login/logout [10 min] 
---
# Přestávka na oběd [30 min - 1 h] 
---
# PID 1 podrobněji [30 min] 
---
# Uživatelské účty a oprávnění uvnitř kontejneru, USER [15 min] 
---
# Publikace portů, EXPOSE [15 min] 
---
# Volumes, mount, VOLUME [15 min] 
---
# Networking, komunikace mezi kontejnery [15 min] 
---
# Přestávka [15 min] 
---
# Docker best practices [25 min] 
---
# Orchestrace – Docker Compose, Docker Swarm [5 min] 
---
# Přidělování CPU a paměti [5 min] 
---
# Kontejnerizace Java aplikace, JIB [55 min] 
---
# Diskuse 
---
# Landslide

.notes: These are my notes, hidden by default

.qr: 450|https://github.com/ionelmc/python-darkslide

.footnote: Slides available at https://blog.ionelmc.ro/presentations/

---

# Overview

Generate HTML5 slideshows from markdown, ReST, or textile.

![python](http://i.imgur.com/bc2xk.png)

Landslide is primarily written in Python, but it's themes use:

- ~~HTML5~~
- **Javascript**
- CSS

---

# Code Sample

Landslide supports code snippets

    !python
    def log(self, message, level='notice'):
        if self.logger and not callable(self.logger):
            raise ValueError(u"Invalid logger set, must be a callable")

        if self.verbose and self.logger:
            self.logger(message, level)

