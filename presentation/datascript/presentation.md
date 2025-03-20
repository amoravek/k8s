# CI/CD: end-to-end řešení pro on-premise aplikace

#
#

<p style="text-align: center;">Adam Morávek<br>amoravek@edgeclusters.com<br>+420 604 538 070</p>

![edgeclusters-logo](edgeclusters-logo.png)
<p style="text-align: center;">edgeclusters.com</p>

.notes: amo-datascript?

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# EdgeClusters.com

<img src="cluster.jpg" alt="cluster" width="500"/>

- Vytváření DevOps stacků na míru
- Konzultace
- Vývoj edge HW (arm)


#
#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Příklad vzdělávacího projektu

DevOps stack pro malý vývojový tým, maximum automatizace

- HA Kubernetes
- "produkční" zabezpečení
- GitHub Actions, vlastní Nexus, Harbor, Keycloak
- kódy i infrastrukutra v Git
- multi-cluster - dynamicky vytvářené clustery (Taikun)
- multi-arch (amd64 + arm64)

#
#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Cluster pro demo

#
#

<table border="0px">
    <tr>
        <td colspan="2"><img src="taikun-arch.webp" alt="taikun-arch" width="800"/></td>
    </tr>    
</table>

<p align="center"><b>https://taikun.cloud</b></p>

#
#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Zamyšlení

#

<p align="center">(mindmapa)</p>

#
#

---

# Demo - architektura

#

<p align="center">(demo-architektura.drawio)</p>

#
#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# GitHub Actions

- GitHub Actions (...)
- obvyklé věci lze vyřešit jednoduše (Actions pro "mainstream")
- s detaily je to horší:
    - "obvyklé" CI funkce ponechány na zpracování v Actions nebo shellu (např. téměř chybějící String functions, pokročilejší podmínky, apod.)
    - testování (act?)
- nutno dobře zvážit pricing plan
    - např. protected branches až v úrovni Team ($40-48 / user / rok)
- pro public repa hodně funkcí zadarmo
    - protected branches
    - workflow templates

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Multi-cluster

- multi-cluster deployment řešen pomocí ArgoCD (Declarative GitOps CD for Kubernetes ...)
- odlišné konfigurace:
    - ingress objekty (host)
    - cert-manager issuers
    - verze instalovaných aplikací (infra, business)
    - storage (výhoda default storage class)
    - ...

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Multi-arch I.

- x86_64 / amd64 stále dominuje, ale objevuje se ARM (a jiné)
    - Amazon Graviton2
    - ...ale i třeba RPI4 a mnohé další SBC, které postupně sílí

- výhody ARM
    - nižší spotřeba
    - lépe se chladí
    - od telefonů, hobby HW až po datacentra
    - v cloudu levnější a výkonnější

- hlavní nevýhody z pohledu běhu SW
    - nekompatibilní s x86
    - nutná podpora výrobců SW

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Multi-arch II.

- typický problém: Exec format error
    - nekompatibilní image / base image
    - nekompatibilní aplikace v kontejneru
    - nekompatibilní = obsahje kód zkompilovaný pro jinou platformu (zde amd64)

- elegantní řešení - multi-arch image
    - obsahuje data pro více platforem
    - správné vrstvy se zvolí na základě metadat od klienta (platform)
    - viz OCI Image Index Specification
    - ukázka v registry

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Multi-arch III.

- s GitHub Actions je situace extrémně snadná (ukázka)


#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Ukázka

<p align="center">multi-cluster multi-arch deployment</p>

#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Závěr

#

- moderní DevOps stacky se velmi složitě instalují i spravují (spousta komponent)
- ArgoCD - užitečný nástroj pro zmírnění komplexit; GitOps jednoduše
- GitHub Actions - škoda, že jen cloud; rafinovaný pricing; velmi jednoduché buildy včetně cache a multi-arch

#

.footer: CI/CD: end-to-end řešení pro on-premise aplikace

---

# Návazný kurz

#

- velká část témat zahrnuta do připravovaného kurzu [CI/CD: end-to-end řešení pro on-premise aplikace](https://www.datascript.cz/kurzy/open-source/cicd-end-to-end-reseni-pro-on-premise-aplikace)

- 31.3. - 1.4. 2022

- kurz je garantovaný

![kurz-qr](kurz-qr.png)


.footer: CI/CD: end-to-end řešení pro on-premise aplikace

#

---

# Dotazy / diskuse 

.footer: CI/CD: end-to-end řešení pro on-premise aplikace
