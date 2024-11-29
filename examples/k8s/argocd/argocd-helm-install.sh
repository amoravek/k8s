helm upgrade --install \
argocd \
argo-cd \
--create-namespace \
--namespace argocd \
--set dex.enabled=false \
--set server.service.type=LoadBalancer \
--repo https://argoproj.github.io/argo-helm
