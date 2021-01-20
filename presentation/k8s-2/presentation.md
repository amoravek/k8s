# Kubernetes II.

Online poznámky: <https://codimd.trask.cz/s/rJPCa8C0v>

Použitý cluster: <https://arm.lab.trask.cz/>

Adam Morávek, amoravek@trask.cz, +420 724 514 916

---

Zabezpečení ingressu pomocí TLS, TLS secrety 

High-availability, pod disruption budget 

Horizontal pod autoscaler 

Labels & selectors, node selectors 

Taints, tolerations 

Cordon, evict 

Custom Resource Definition (CRD) 

Resource quota 

Resource limit 

Kubernetes best practices 

Autentifikace, uživatelské a servisní účty 

RBAC 

Multiplatformní přístupy (ARM64) 

---
# Přestávka

.footer: [15 min]

![docker-vs-vm](../common/coffee.jpg)

---
# Perzistence (1)

.footer: [20 min] 

- aby bylo možné uchovávat data produkovaná kontejnery v Podu, je potřeba je někam uložit (jinak zaniknou spolu s Podem)
- na úrovni Podu (ukázat) je možné definovat externí úložiště - např.:

        !yaml
        volumes:
        - name: test-volume
          hostPath:
            path: /data-xxx
            type: DirectoryOrCreate

- aby bylo dostupné v *kontejneru*, je potřeba provést mount:

        !yaml
        volumeMounts:
        - name: test-volume
          mountPath: /data

- každý kontejner může provést mount téhož volume do jiného adresáře

<https://kubernetes.io/docs/concepts/storage/volumes/>

---
# Perzistence (2)

- předchozí příklad sice funguje, ale takový přístup má řadu nedostatků:
    - mixuje odpovědnost storage admina a vývojáře/devops inženýra
    - nelze nastavit oprávnění na úrovni Kubernetes
    - mnohdy je nutné zadat i přihlašovací údaje diktované volume pluginem
    - pokud je potřeba použít volume i z jiného Podu, je nutné duplikovat konfiguraci

Řešením je použíti PersistentVolume a PersistentVolumeClaim - příklady (examples/k8s/pv-pvc)

---
# StorageClass

Jde to ale ještě snáze - pomocí StorageClass v kombinaci se storage autoprovisioningem
(a admin se do celého procesu vůbec nemusí montovat).

Příklad (examples/k8s/storageclass)

---
# ConfigMap, Secret

.footer: [15 min] 

- i když se mechanismy perzistence dají použít k přístupu ke konfiguračním souborům, není to dobrá praxe
- existují totiž ConfigMap a Secret objekty - uchovávají
    - textové i binární soubory
    - proměnné prostředí
    - citlivá data
    - PKI artefakty (certifikáty, klíče)
- je tu tedy další možnost separovat odpovědnost a vyjít vstříc CI/CD principům

Příklad (examples/k8s/configmap-secret)

---
# Services

.footer: [15 min] 

- pody mají nestabilní IP i DNS name (=název Podu)
- Objekt Service představuje elegantní způsob řešení:

    - `kubectl create deployment nginx --image=nginx`
    - `kubectl expose deployment nginx --port 8888 --target-port 80`

- ClusterIP - pouze interně v rámci clusteru
- NodePort - jednoduchý, funkční...ale ty porty...navíc obsadí porty na všech nodech
- LoadBalancer - nejpohodlnější, ale v cloudu drahý (samostatná IP)

<https://kubernetes.io/docs/concepts/services-networking/service/>

---
# Ingress a ingress controller

.footer: [20 min] 

- objekt Ingress je pravidlo pro reverzní proxy deklarované v YAML
- deklarujeme tedy pravidlo a o ostatní se postará reverzní proxy...a ta je kde?
- je potřeba nainstalovat *ingress controller* (typicky kontejnerizovaný nginx nebo haproxy)
- funguje jen pro HTTP
- jako backend je použita Service typu ClusterIP

Příklad (examples/k8s/ingress)

Závěr: **pro HTTP je idealní vystavit ingress controller jako Service typu LoadBalancer a pro ostatní komunikaci použít buď LoadBalancer (drahé) nebo NodePort (levné, ale v Cloudu komplikované)**

<https://kubernetes.io/docs/concepts/services-networking/ingress/>

---
# Přestávka na oběd

.footer: [30 min - 1 h] 

![obed](../common/open.knedliky.jpg)

---
# Controllery

.footer: [10 min] 

- controllery udržují v Clusteru deklarovaný (požadovaný) stav (např. počet replik Podu)
- každý controller řeší jinou úlohu
- správu controllerů provádí komponenta Controller-manager
- nejpoužívanější controller je Deployment

<https://kubernetes.io/docs/concepts/architecture/controller/>

Příklady:

<https://kubernetes.io/docs/concepts/workloads/controllers/>

---
# ReplicaSet, DaemonSet, StatefulSet, Job, CronJob

.footer: [20 min] 

Příklady: examples/controllers

---
# Logování

.footer: [10 min] 

---
# Liveness a readiness testy

.footer: [15 min] 

(build image + sestavit celý deployment)

- každý kontejner by měl mít deklarován způsob, jakým Kubelet pozná, že se správně inicializoval a zda funguje.
- readiness probe = testuje, zda je kontejner připraven přijímat požadavky
- liveness probe = periodicky testuje kontejner, zda je živý a zdravý
- typy: <https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.20/#probe-v1-core>

**Bez těchto testů nemůžeme mluvit o HA!**

Příklad: examples/k8s/readiness-liveness

<https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/>

---
# ImagePullPolicy

- imagePullPolicy: IfNotPresent vs Always
- AlwaysPullImages adminssion controller

.footer: [10 min] 

<https://kubernetes.io/docs/concepts/containers/images/#updating-images>

---
# Resource requests & limits

.footer: [15 min] 

- Aby mohl Scheduler optimalizovat využití zdrojů jednotlivých nodů, potřebuje k tomu v ideálním případě znát miniální požadavky aplikace na CPU a paměť
- u každého kontejneru by měla být sekce resources, např.:

    !yaml
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"

---
# Diskuse 
