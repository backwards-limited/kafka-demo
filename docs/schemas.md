# Schemas

## Post New Schema

```bash
$ curl -X POST -H "Content-Type:
application/vnd.schemaregistry.v1+json" \
    --data '{"schema": "{\"type\": …}’ \
    http://localhost:8081/subjects/Employee/versions
```

## View Schemas

```bash
$ curl http://localhost:8081/subjects
```

