# Kubernetes II.

---
# Organizační informace

Online poznámky: <https://codimd.trask.cz/s/rJPCa8C0v>

Adam Morávek<br/>
amoravek@trask.cz<br/>
+420 724 514 916<br/>

---
# Requests, limits v k9s

- ukázka v k9s

---
# kubectl ... -o jsonpath

`kubectl get pod k8s-sample-app-648d688cfc-8h7hx -o jsonpath='{.spec.nodeName}'`

`kubectl get pod k8s-sample-app-648d688cfc-8h7hx -o jsonpath='{range .spec.containers[*]}{.name}{"\t"}{.image}'`

<https://kubernetes.io/docs/reference/kubectl/jsonpath/>

---
# Labels & selectors v CLI

- `kubectl get po --selector mujlabel=hodnota`

---
# kubectl api-resources

- `kubectl api-resources`

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
# kubeconfigy (kdo nemá)

---
# RBAC (Role-Based Access Control)

- dá se vypnout (standardně bývá zapnuto - viz apiserver param --authorization-mode)
- příklad - Users + Group 'skoleni'
- Role - oprávnění pouze v rámci namespace
- ClusterRole - oprávnění ve všech ns + na non-namespaced (cluster-wide) objekty
- RoleBinding
- ClusterRoleBinding

Seznam verbs: `kubectl api-resources --sort-by name -o wide`

`kubectl api-resources --api-group ""`

<https://kubernetes.io/docs/reference/access-authn-authz/rbac/>

---
# Příklad role

    !yaml
    apiVersion: rbac.authorization.k8s.io/v1
    kind: Role
    metadata:
      name: pod-reader
      namespace: amoravek
    rules:
    - apiGroups:
      - ""
      resources:
      - pods
      - services
      verbs:
      - get
      - list
      - watch

`kubectl create role pod-reader --resource "pods,service" --verb "get,list,watch"`

kde vzít `apiGroups`?

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
- ukázka výpadků v případě chybějící readiness proby

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

      kubectl create quota my-quota \
      --hard=cpu=1,memory=1G,pods=2,services=3,replicationcontrollers=2,resourcequotas=1,secrets=5,persistentvolumeclaims=10

<https://kubernetes.io/docs/concepts/policy/resource-quotas/>

---
# kubectl patch (1)

`kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "harbor-dockerhubproxy"}]}'`

`kubectl patch deployment k8s-sample-app -p '{"metadata":{"labels":{"demo":"yes"}}}'`

jenže co když chceme postupně přidávat více (přes CLI)?
- json: `kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "harbor-amor"}, {"name": "harbor-dockerhubproxy"}]}'`
  
---
# kubectl patch (2)

- patch file: `kubectl patch serviceaccount default --patch-file ...`

        imagePullSecrets:
        - harbor-amor
        - harbor-dockerhubproxy
- type=json:
  
      kubectl patch nodes worker04 worker05 \
      --type=json -p='[{"op": "add", "path": "/metadata/labels/demo", "value": "yes"}]'

      kubectl patch nodes worker04 worker05 \
      --type=json -p='[{"op": "remove", "path": "/metadata/labels/demo"}]'

...nebo dohromady:

      kubectl patch nodes worker04 worker05 \
      --type=json -p='[
        {"op": "add", "path": "/metadata/labels/demo", "value": "yes"},
        {"op": "remove", "path": "/metadata/labels/xxx", "value": "yes"}
      ]'

<https://kubernetes.io/docs/tasks/manage-kubernetes-objects/update-api-object-kubectl-patch/>

---
# Zabezpečení ingresu pomocí TLS

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
# Taints, tolerations (1)

  `kubectl taint nodes node1 key1=value1:NoSchedule`

  `kubectl taint nodes node1 key1=value1:NoSchedule-`

    tolerations:
    - key: "key1"
      operator: "Equal"
      value: "value1"
      effect: "NoSchedule"
      tolerationSeconds: 300

    tolerations:
    - key: "key1"
      operator: "Equal"
      value: "value1"
      effect: "NoExecute"

    tolerations:
    - key: "key1"
      operator: "Exists"
      effect: "NoSchedule"

---
# Taints, tolerations (2)

`kubectl describe nodes | egrep -hi "Taint|Hostname"`

`kubectl get nodes -o custom-columns=NAME:.metadata.name,TAINTS:.spec.taints --no-headers`

<https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/>

---
# Pod disruption budget 

- vysvetlit

---


# HA, pod disruption budget (příprava 1)

  1) nejprve vyčistit 2 nody (vysvětlit):

          kubectl patch nodes worker04 worker05 -p '{"spec":{"taints": null}}'
          kubectl patch nodes worker04 worker05 --type=json -p='[{"op": "remove", "path": "/metadata/labels/demo"}]'

          kubectl taint node worker04 mytaint=pdb-test:NoExecute
          kubectl taint node worker05 mytaint=pdb-test:NoExecute
          kubectl taint node worker05 mytaint=pdb-test:NoSchedule

  2) přidat label `demo=yes` oběma nodům (třeba přes patch nebo kubectl label ...)

    - `kubectl patch nodes worker04 worker05 --type=json -p='[{"op": "add", "path": "/metadata/labels/demo", "value": "yes"}]'`
  
    - zkontrolovat přes --selector

---
# HA, pod disruption budget (příprava 2)

  3) nodeSelector -> node label demo=yes
    - na oba nody
    - nasadit deployment
    - naškálovat na 2 repliky
  
  4) dodat toleration do deploymentu:

          tolerations:
          - key: "mytaint"
            operator: "Equal"
            value: "pdb-test"
            effect: "NoExecute"

      cílem je dostat pody jen na worker04
  
  5) osdtranit taint NoSchedule (proč?)

  6) předvést drain worker04 při zapnuté curl smyčce (app-client = nginx terminal)

          while true; do
            curl http://k8s-sample-app/ready
            cat /proc/uptime|awk '{print $1}'
            sleep 0.1
          done

---
# HA, pod disruption budget

  `kubectl create poddisruptionbudget k8s-sample-app-pdb --selector=app=k8s-sample-app --min-available=2`

- app-client + skript
- drain bez PDB
- drain s PDB

<https://kubernetes.io/docs/tasks/run-application/configure-pdb/>
<https://kubernetes.io/docs/concepts/workloads/pods/disruptions/>

POZOR! PDB nefunguje v případech **taint-based eviction** (vysvětlit a ukázat)

---
# Horizontal pod autoscaler 

`kubectl autoscale deployment k8s-sample-app --min=1 --max=3 --cpu-percent=50`

load:

`kubectl run -i --tty load-generator --rm --image=harbor.trask.cz/arm64/ubuntu-netcat --restart=Never -- /bin/sh -c "while sleep 0.01; do curl -s --connect-timeout 2 --max-time 2 http://10.4.61.4; done"`


<https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/>
<https://v1-25.docs.kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/>

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
# Headless service

- service, která neslouží k loadbalancingu, ale k mapování IP podů
- DNS A záznamy (ukzat nslookup, dig - app-client - apt install dnsutils)

---
# StatefulSet



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