# Kubernetes II.

Online poznámky: <https://codimd.trask.cz/s/rJPCa8C0v>

Použitý cluster: <https://arm.lab.trask.cz/>

Adam Morávek, amoravek@trask.cz, +420 724 514 916

---

# Readiness a Liveness probes - pokračování

---
# Zabezpečení ingressu pomocí TLS, TLS secrety 

.notes: projit ingress + tls secret entry-app

---
# Labels & selectors, node selectors 

---
# High-availability, pod disruption budget 

examples/k8s/ha

---
# Horizontal pod autoscaler 

---
# Taints, tolerations 

---
# Cordon, drain 

  kubectl drain worker05 --ignore-daemonsets --delete-local-data
  
  watch -n 1 curl -s --connect-timeout 2 --max-time 2 http://10.4.61.4

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
# Přestávka

.footer: [15 min]

![docker-vs-vm](../common/coffee.jpg)

---
# Přestávka na oběd

.footer: [30 min - 1 h] 

![obed](../common/open.knedliky.jpg)

---
