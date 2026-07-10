# Helm chart (Block 4, §4) — you build this one yourself

Deliberately not scaffolded: creating the chart IS the lesson.

1. `helm create product-service` here, study everything it generated
   (templates/deployment.yaml, _helpers.tpl, values.yaml).
2. Replace the generated templates with parameterized versions of
   ../k8s/product-service/*.yaml — move image tag, replicaCount, resources,
   probe paths, and the ConfigMap values into values.yaml.
3. Lifecycle drill:
   ```powershell
   helm install product ./product-service
   helm upgrade product ./product-service --set replicaCount=3
   helm history product
   helm rollback product 1
   helm template ./product-service        # render without installing — debug tool
   ```
Know for interviews: chart / release / revision, values precedence
(-f file < --set), how rollback works (stored release manifests in cluster
secrets), chart dependencies (Chart.yaml dependencies:), and where Helm ends
and GitOps (ArgoCD) begins in production.
