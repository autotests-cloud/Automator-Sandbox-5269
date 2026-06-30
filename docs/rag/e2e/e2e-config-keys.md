---
id: e2e-config-keys
domain: e2e
phase: 4a
adr: 002
tags: [config, properties, selenide, allure, testops]
---
# Config keys e2e

Ключи `TestConfig` / `config/*.properties`. Override: `-Dkey=value` или `-Denv=profile`.

Три независимых слоя — не смешивать:

| Слой | Ключи | Назначение |
|------|-------|------------|
| **Allure Report** | `allureReportMode` | Писать `build/allure-results` + локальный HTML |
| **Allure runtime** | `attach*`, `enableAllureSelenideListener` | Listener + attachments в `@AfterEach` |
| **Console log** | `logToConsole`, `selenideLogToConsole`, `rootLogLevel` | JVM/Selenide stdout; ≠ `attachBrowserConsoleLogs` |

## Run / driver

| Key | Default | Назначение |
|-----|---------|------------|
| `env` | `local` | `config/${env}.properties` |
| `baseUrl` | `""` | HTTP(S) корень |
| `basePath` | `""` | Локальная папка → `file://` |
| `browser` | `chrome` | |
| `browserVersion` | `148` | remote |
| `browserSize` | `1920x1280` | |
| `headless` | `false` | |
| `closeBrowserAfterEach` | `false` | |
| `includeTags` | `""` | JUnit `@Tag` include (comma-separated), напр. `visual` |
| `excludeTags` | `""` | JUnit `@Tag` exclude (comma-separated) |
| `remoteUrl` | `""` | Selenoid / Grid |
| `enableVnc`, `enableVideo`, `enableHar` | `false` | selenoid:options |
| `videoFolder` | `""` | prefix для video URL |

## Allure Report

| Key | Default | Назначение |
|-----|---------|------------|
| `allureReportMode` | `allure3` | `none` — adapter off; `allure2` — results + `allure serve`; `allure3` — results + `gradle allureReport` + `allurerc.json` |

Gradle: `allure.adapter.autoconfigure=false` при `none`.

## Allure runtime

| Key | Default | Назначение |
|-----|---------|------------|
| `enableAllureSelenideListener` | `false` | Auto-steps Selenide → Allure |
| `attachBrowserConsoleLogs` | `false` | browser console → Allure attachment |
| `attachPageSource`, `attachLastScreenshot`, `attachHarLogs`, `attachVideo` | `false` | opt-in artifacts |
| `updateBaselines` | `false` | Перезаписать baselines PNG для `@Tag("visual")`; см. `visual-baseline` |
| `baselinesDir` | `screenshots` | Подкаталог `src/test/resources/` с baseline PNG |
| `visualDiffThreshold` | `0.015` | Max pixel diff ratio; `-DvisualDiffThreshold=0.02` |

## Console log

| Key | Default | Назначение |
|-----|---------|------------|
| `logToConsole` | `true` | мастер-переключатель stdout |
| `selenideLogToConsole` | `true` | Selenide `SimpleReport` (не AllureSelenide) |
| `rootLogLevel` | `info` | `org.slf4j.simpleLogger.defaultLogLevel` |

## TestOps (env, не TestConfig)

Ортогонально локальному HTML. Имена как в qa-guru-home CI:

| Env | Пример |
|-----|--------|
| `ALLURE_ENDPOINT` | `https://allure.autotests.cloud` |
| `ALLURE_PROJECT_ID` | per project (напр. `5263`) |
| `ALLURE_TOKEN` | secret, не в git |
| `ALLURE_RESULTS` | `build/allure-results` |
| `TEST_CASE_ID` | `@AllureId` / launch name |

Прогон: `allurectl watch --results build/allure-results -- ./gradlew test …`

## Don't

- `attachHarLogs=true` — stub, бросает exception.
- `allureReportMode=none` + `attach*=true` или TestOps — конфликт (e2e-builder).
- CI/TestOps/Selenoid bootstrap — соседний workspace `qa-guru-home/`, не копировать workflows в template-project.
