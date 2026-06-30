---
id: test-style-ladder
domain: e2e
phase: 4a
adr: 002
tags: [selenide, junit, allure]
---
# Учебная градация

**id:** `test-style-ladder`

## Файлы

**Curriculum (code reference):** `tests-java-curriculum/src/test/java/tests/LoginTests.java`, `LogoutTests.java`

**Канон smoke:** `tests-java/src/test/java/tests/LoginTests.java` — только `shouldLoginWithValidCredentials`

## Входы

—

## Assert

negative и smoke проходят (curriculum)

## Do

- smoke → PO (`shouldLoginWithValidCredentials`) — есть и в curriculum, и в каноне
- negative ladder (curriculum only): raw none → nested manual step → listener ON → listener explicit OFF → TestOps manual (`shortLoginAuthorizationTest`)
- logout (curriculum): form login vs localStorage shortcut — чанк `test-logout-flow`
- закомментированный raw Selenide — baseline «до PO»
- manual TestOps vs exploratory — чанк `test-manual`

## Don't

- Требовать PO везде в учебном репо
- Смешивать ручные `Allure.step` и `AllureSelenide` в одном методе
- Искать ladder-методы в `tests-java/LoginTests` — там один smoke (чанк `test-pyramid`)
