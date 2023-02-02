helm upgrade --install \
argocd \
argo/argo-cd \
--create-namespace \
--namespace argocd \
--set dex.enabled=false \
--set server.service.type=LoadBalancer
