---
id: test-storage-shortcut
domain: e2e
phase: 4a
adr: 002
tags: [selenide, junit, allure]
---
# Bypass UI login

**id:** `test-storage-shortcut`

## Файлы

`tests-java/src/test/java/pages/LoggedInPage.java` — `openPageWithLocalStorageAuthentication`

**Канон visual:** `LoggedInBaselineTests` (`@Tag("visual")`)

**Curriculum logout chain:** `tests-java-curriculum/.../LogoutTests.successfulLogoutWithLocalStorageAuthenticationTest` — чанк `test-logout-flow`

## Входы

authUser, userbase

## Assert

Welcome без submit

## Do

Ключи из app one-page-form

## Don't

Выдумывать keys; несуществующие URL

