# Kubernetes II.

Online poznámky: <https://codimd.trask.cz/s/rJPCa8C0v>

Použitý cluster: <https://arm.lab.trask.cz/>

Adam Morávek, amoravek@trask.cz, +420 724 514 916

---
# Autentizace - pokračování

- User vs. ServiceAccount

---
# Autentizace - x509

- kubeconfig - podrobněji
- Kubernetes PKI (viz k8s/examples/k8s/security/create-user.sh)
  - nutny cluster admin
- User a Group - vazba na DN (opět viz create-user.sh)

<https://kubernetes.io/docs/tasks/tls/managing-tls-in-a-cluster/>

---
# Autentizace - bootstrap tokens

`kubeadm token create` -> `lsvnin.y4cc92mo4zrd7ea7` (`<user>.<secret>`)

(user = lsvnin, secret = y4cc92mo4zrd7ea7)

`system:bootstrap:<user>`

`system:bootstrappers` - skupina, kde jsou všechny `system:bootstrap:*` účty

Token buď do .kube/config nebo `kubectl ... --token <token>`

---
# Autentizace - bootstrap tokens (příklady)

`kubectl create rolebinding system:bootstrap:lsvnin-default-admin --clusterrole cluster-admin --user system:bootstrap:lsvnin -n default`

`kubectl create rolebinding bootstrapers-default-admin --clusterrole admin --group system:bootstrappers --namespace default`

`kubectl create clusterrolebinding system:bootstrap:lsvnin-cluster-admin --clusterrole cluster-admin --user system:bootstrap:lsvnin`

<https://kubernetes.io/docs/reference/access-authn-authz/bootstrap-tokens/>

---
# Autentizace - Service Account Tokens

`kubectl create serviceaccount jenkins`

`kubectl create token jenkins`

User: `system:serviceaccount:(NAMESPACE):(SERVICEACCOUNT)`

Group: `system:serviceaccounts`

---
# OpenID Connect

<https://kubernetes.io/docs/reference/access-authn-authz/authentication/#openid-connect-tokens>

---
# RBAC (Role-Based Access Control)

- dá se vypnout (default = 
- příklad - Users + Group 'skoleni'
- Role
- ClusterRole
- RoleBinding
- ClusterRoleBinding

Seznam verbs: `kubectl api-resources --sort-by name -o wide`

---
# RBAC - cvičení 1

- nová role pro PKI (namespaced)
  - CertificateSigningRequest
  - certificate approve

<https://kubernetes.io/docs/reference/access-authn-authz/certificate-signing-requests/>

`kubectl api-resources --verbs=list --api-group certificates.k8s.io -o wide`

---
# Řešení

`kubectl create clusterrole pki-admin --resource certificatesigningrequest.certificates.k8s.io --verb create,get,list,watch`

`kubectl create clusterrolebinding pki-admin-amo-amoravek --clusterrole pki-admin --namespace amo --user amoravek`

Proč **cluster**rolebinding a ne rolebinding?
Protože `certificatesigningrequest` není *namespaced* resource.

Approval:

Verbs: update, group: certificates.k8s.io, resource: certificatesigningrequests/approval

Verbs: approve, group: certificates.k8s.io, resource: signers, resourceName: <signerNameDomain>/<signerNamePath> or <signerNameDomain>/*

---
# RBAC - cvičení 2

- umožnit všem uživatelům naráz získat informace o nodech (kubectl get/describe nodes)
- Hint: všichni jsou ve skupině `skoleni` --> `--group`

---
# Řešení

`kubectl create clusterrolebinding skoleni-view --group skoleni --clusterrole view`

---
# Readiness a Liveness probes - pokračování

- kompletní ukázka chování při výpadku liveness i readiness probe

---
# Hledání problémů

- scaling - neudělá požadovaný počet replik
  - kontrola logů controller-managera; možné příčiny
    - kvóty
    - existuje PodDisruptionBudget

---

# LimitRange 

- default limits

<https://kubernetes.io/docs/concepts/policy/limit-range/>

---

# ResourceQuota

- PriorityClass

<https://kubernetes.io/docs/concepts/policy/resource-quotas/>

---
# kubectl patch

`kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "harbor-dockerhubproxy"}]}'`

jenže co když chceme postupně přidávat více (přes CLI)?
- json: `kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "harbor-amor"}, {"name": "harbor-dockerhubproxy"}]}'`
  
- patch file: `kubectl patch serviceaccount default --patch-file ...`

        imagePullSecrets:
        - harbor-amor
        - harbor-dockerhubproxy

<https://kubernetes.io/docs/tasks/manage-kubernetes-objects/update-api-object-kubectl-patch/>

---
# Zabezpečení ingressu pomocí TLS

- TLS secrety - viz entry app (ukázka vytvoření TLS secretu)
- Registry secrety
- generické secrety

.notes: projit ingress + tls secret entry-app

---
# Labels & selectors, node selectors

- label je prostá nálepka
- selectory jsou filtry

.notes: ukazat na examples/ha - je tam selector i nodeSelector, dal ukazat i cli --selector

<https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/>

---
# High-availability, pod disruption budget 

  `kubectl create poddisruptionbudget k8s-sample-app-pdb --selector=app=k8s-sample-app --min-available=2`

.notes: examples/k8s/ha

.notes: ukazat nejprve drain bez pdb, pak s nim

<https://kubernetes.io/docs/tasks/run-application/configure-pdb/>

****---
# Horizontal pod autoscaler 

`kubectl autoscale deployment k8s-sample-app --min=1 --max=3 --cpu-percent=50`

load:

`kubectl run -i --tty load-generator --rm --image=harbor.trask.cz/arm64/ubuntu-netcat --restart=Never -- /bin/sh -c "while sleep 0.01; do curl -s --connect-timeout 2 --max-time 2 http://10.4.61.4; done"`


<https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/>
<https://v1-25.docs.kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/>

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
# Kubernetes best practices -> Helm Charts

---
# Autentifikace, uživatelské a servisní účty 

https://kubernetes-tutorial.schoolofdevops.com/configuring_authentication_and_authorization/

---
# Container registry - autentifikace

.notes: default/docker-registry-harbor

.notes: objasnit princip sa + registry secret

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
