# TaskFlow API

API REST de gestion de tâches collaborative, inspirée des boards Kanban (Trello / Jira light).

[![CI](https://github.com/YOUR_USERNAME/taskflow-api/actions/workflows/ci.yml/badge.svg)](https://github.com/YOUR_USERNAME/taskflow-api/actions/workflows/ci.yml)
[![GitHub Pages](https://img.shields.io/badge/docs-GitHub%20Pages-blue)](https://YOUR_USERNAME.github.io/taskflow-api/)

## À propos

TaskFlow permet de gérer des **projets**, des **colonnes Kanban**, des **tâches** et des **commentaires** via une API sécurisée par JWT. Le projet met en avant les bonnes pratiques backend : architecture en couches, migrations Flyway, tests d'intégration, Docker et CI GitHub Actions.

## Fonctionnalités

- Authentification JWT
- Projets collaboratifs avec board Kanban
- Tâches assignables avec priorités et échéances
- Commentaires sur les tâches
- Documentation OpenAPI / Swagger UI

## Stack

Spring Boot 3 · Java 21 · PostgreSQL · Docker · Testcontainers

## Documentation

La page d'accueil du projet détaille l'architecture, les endpoints et le guide de démarrage :

**https://YOUR_USERNAME.github.io/taskflow-api/**

En local, une fois l'API lancée : http://localhost:8080

## Licence

MIT
