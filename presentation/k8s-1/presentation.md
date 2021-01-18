# Kubernetes

---
# Potřeba orchestrace kontejnerů

.footer: [5 min]

- organizovat několik málo kontejnerů s jednoduchou aplikací ješte celkem jde
- ale co dělat v momentě, kdy existuje celý ekosystém kontejnerů se specifickými potřebami na:
    - dostupnost
    - propustnost
    - bezpečnost
    - spravovatelnost
    - škálování
    - a mnohé další?
- je nevyhnutelné, aby existoval nástroj, který jednotnou cestou řeší tyto a mnohé další potřeby

---
# Kubernetes

.footer: [5 min]

- zavádí **deklarativní** přístup ke správě kontejnerizovaných aplikací
- v současné době jediná platforma, která dává dlouhodobý smysl jako cílová platforma pro kontejnerizované aplikace (můj osobní názor)
- snadno rozšiřitelný a modulární
- přenositelný téměř na jakýkoliv HW (viz https://arm.lab.trask.cz)
- velmi dobře navržený
- velmi dobře testovaný
- velmi dobře ***zdokumentovaný***
- bohužel ale...**velmi složitý**

---
# Příklad deklarativního způsobu konfigurace (YAML)

.notes: entry-backend

---
# Architektura Kubernetes (1)

.footer: [10 min] 

![k8s-arch.png](k8s-arch.png)

---
# Architektura Kubernetes (2)

- API server: centrální vstupní bod clusteru, REST API
- Scheduler: plánuje workload na konkrétní nody a hlídá jejich systémové zdroje (CPU, RAM, disk, ...)
- Controller-manager: *hlídá* stav (controllerů) clusteru (deklarovaný stav vs reálný stav)
- etcd: *uchovává* stav clusteru
- Kubelet: "node agent" - hlídá pody, jejich kontejnery, stav node
- Kube-proxy: na každém node směruje příchozí provoz 
- Container runtime (zde Docker): runtime pro běh kontejnerů - viz dále CNI

---
# Pod a jeho kontejnery

.footer: [10 min] 

- základní abstrakcí Kubernetes je *Pod*
- Pod je nejmenší "nasaditelný" objekt, takový clusterový "atom"
- V Podu jsou seskupeny kontejnery (jeden nebo více)
- Pod je vždy schedulerem plánován se všemi svými kontejnery (kolokace)
- Kontejnery v Podu nejsou izolované, ale sdílí kontext (opět Linux namespaces a cgroups)
- Kontextem rozumíme např. sdílené úlořiště (volumes) nebo sdílený síťový namespace (komunikace přes localhost, sdílený prostor pro porty)

(Příklad: examples/k8s/k8s-shared-network-ns - při delete je tam znatelná prodleva - PID 1)

<https://kubernetes.io/docs/concepts/workloads/pods/>

---
# Namespaces

.footer: [5min] 

- Kubernetes namespace slouží k izolaci objektů a zdrojů
- ale také k plošnému nastavení různých kvót, limitů a autorizací:

    `kubectl describe namespace amo`

---
# Kubectl

.footer: [20 min] 

- CLI rozhraní pro komunikaci s clusterem (resp API-serverem)
- vyžaduje přítomnost konfiguračniho souboru, tzv. *kubeconfig* (uložen typicky v $HOME/.kube/config) - viz váš osobní kubeconfig rozesílaný před kurzem
- kubeconfig obsahuje sady clusterů a uživatelů propojených kontexty
- Praktické ukázky

---
# Příklad spuštění aplikace v podu – kubectl run + průzkum

.footer: [20 min] 

- nejjednodušší způsob nasazení aplikace do Kubernetes:
    - `kubectl run app1 --image=nginx`
    - `kubectl create deployment app1-deployment \
        --image=nginx --replicas=2`

---
# K9s, Kubernetes Web IU (Dashboard)

.footer: [10 min] 

---
# Deployment

.footer: [10 min] 

---
# Přestávka

.footer: [15 min] 

---
# Příklad deploymentu – kubectl run + průzkum

.footer: [20 min] 

---
# ConfigMap, Secret

.footer: [15 min] 

---
# Services – ClusterIP, NodePort, LoadBalancer

.footer: [15 min] 

---
# Ingress a ingress controller

.footer: [20 min] 

---
# Perzistence, StorageClass

.footer: [20 min] 

---
# Přestávka na oběd

.footer: [30 min - 1 h] 

---
# Controllery

.footer: [10 min] 

---
# ReplicaSet, DaemonSet, StatefulSet, Job, CronJob

.footer: [20 min] 

---
# Logování

.footer: [10 min] 

---
# Liveness a readiness endpointy

.footer: [15 min] 

---
# ImagePullPolicy

.footer: [10 min] 

---
# Resource requests & limits

.footer: [15 min] 

---
# Přestávka

.footer: [15 min] 

---
# Nasazení demo aplikace + průzkum 

---
# Diskuse 