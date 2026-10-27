# Orden de repositorios

Nombres reservados:

1. `estela-api` - backend Spring Boot.
2. `estela-web` - frontend.
3. `estela-infra` - Docker, SonarQube y JMeter.
4. `estela-docs` - ERS, plan, matriz e informe.
5. `estela-qa` - evidencias y reportes.

Convencion de ramas:

- `main`: estable y demostrable.
- `pruebas/UNIT`, `pruebas/SEG`, `pruebas/REND`, `pruebas/ACC`: una rama por ciclo de prueba.
- `evidencia/CP-XX`: capturas y reportes de un caso.

Convencion de commits:

- `feat(api): agregar cancelacion de pedido`
- `test(api): cubrir stock insuficiente`
- `docs(ers): definir requerimientos legales`
- `chore(infra): agregar sonarqube`

Crear repositorios vacios en GitHub y luego:

```bash
cd estela-api
git init
git add .
git commit -m "feat(api): version inicial de Estela"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/estela-api.git
git push -u origin main
```

Repetir el mismo orden para `estela-web`, `estela-infra`, `estela-docs` y `estela-qa`.
No subir `target/`, `node_modules/` ni credenciales reales.
