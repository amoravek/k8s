# Kubernetes II.

Online poznámky: <https://codimd.trask.cz/s/rJPCa8C0v>

Použitý cluster: <https://arm.lab.trask.cz/>

Adam Morávek, amoravek@trask.cz, +420 724 514 916

---
# Odpovědi na některé Dotazy

`kubectl config view --minify|grep namespace`

---
# Readiness a Liveness probes - pokračování

- kompletní ukázka chování při výpadku liveness i readiness probe

---
# TLS

- Zabezpečení ingressu pomocí TLS
- TLS secrety 
- Registry secrety

.notes: projit ingress + tls secret entry-app

---
# Labels & selectors, node selectors

- label je prostá nálepka
- selectory jsou filtry

.notes: ukazat na examples/ha - je tam selector i nodeSelector, dal ukazat i cli --selector

<https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/>

---
# High-availability, pod disruption budget 

  `kubectl create poddisruptionbudget k8s-sample-app-pdb --selector=app=k8s-sample-app --min-available=1`

.notes: examples/k8s/ha

.notes: ukazat nejprve drain bez pdb, pak s nim

<https://kubernetes.io/docs/tasks/run-application/configure-pdb/>

---
# Horizontal pod autoscaler 

`kubectl autoscale deployment k8s-sample-app --min=1 --max=3 --cpu-percent=50`

load:

`kubectl run -i --tty load-generator --rm --image=harbor.trask.cz/arm64/ubuntu-netcat --restart=Never -- /bin/sh -c "while sleep 0.01; do curl -s --connect-timeout 2 --max-time 2 http://10.4.61.4; done"`


<https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/>

---
# Taints, tolerations 

  `kubectl taint nodes node1 key1=value1:NoSchedule`

  `kubectl taint nodes node1 key1=value1:NoSchedule-`

  !yaml
  tolerations:
  - key: "key1"
    operator: "Equal"
    value: "value1"
    effect: "NoSchedule"

  !yaml
  tolerations:
  - key: "key1"
    operator: "Exists"
    effect: "NoSchedule"

.notes: kubectl describe nodes | egrep -hi "Taint|Hostname"

<https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/>

---
# Cordon, drain 

- cordon: zamezení plánování na takto označený node
- drain: "vypuštění" node před údržbou (nebo při vyčerpání zdrojů)
- při drain se automaticky provede cordon:

    `kubectl drain worker05 --ignore-daemonsets --delete-local-data`

    `watch -n 1 curl -s --connect-timeout 2 --max-time 2 http://10.4.61.4`

---
# Custom Resource Definition (CRD) 

---
# Resource quota 

---
# Resource limit 

---
# Kubernetes best practices 

---
# Autentifikace, uživatelské a servisní účty 

---
# Container registry - autentifikace

.notes: default/docker-registry-harbor
.notes: objasnit princip sa + registry secret

---
# RBAC 

---
# Helm Charts

---
# Multiplatformní přístupy (ARM64) 

---
# Diskuse

---
# Přestávka do 10:30

.footer: [15 min]

![docker-vs-vm](../common/coffee.jpg)

---
# Přestávka na oběd

.footer: [30 min - 1 h] 

![obed](../common/open.knedliky.jpg)

---
