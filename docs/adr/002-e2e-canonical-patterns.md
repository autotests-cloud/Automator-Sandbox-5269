# ADR 002: E2e эталон — login form

**Статус:** принято  
**Дата:** 2026-06-30

## Контекст

Фаза 4 template-project: переносимый эталон e2e для портирования на другие языки/стеки.

Паттерны для retrieval вынесены в **`docs/rag/e2e/`** (`manifest.jsonl`). ADR фиксирует решение и scope; чанки — как реализовать.

Roadmap (`docs/CONTEXT.md`, `layout-standard.md`) упоминает **header smoke / visual tests**. Текущий `tests-java/` после очистки содержит **login-сценарии** на reference app [one-page-form](https://github.com/qa-guru/one-page-form), не header.

## Решение

### Scope фазы 4a (этот ADR)

1. **`tests-java/`** — единственный канон стиля и структуры e2e в template-project.
2. **Target app:** `frontend/` — единый static root (`login.html`, `header.html`, …); prod `baseUrl` на GitHub Pages one-page-form.
3. **Сценарии:** login e2e smoke в каноне; negative/ladder/logout patterns — curriculum (`tests-java-curriculum/`). Header smoke — **фаза 4b**, ADR `003`.
4. **Testing pyramid (4.pyramid):** unit / component / integration / e2e / manual в `tests-java/` — чанк `test-pyramid`.

### Слои (stack-agnostic)

| Слой | Назначение |
|------|------------|
| `config/` | Env profiles, typed keys, merge system props |
| `TestBase` | Driver setup once; teardown artifacts + close driver |
| `pages/` | Page Objects, locators, `@Step` |
| `tests/` | JUnit classes, Allure taxonomy, tags |
| `allure/` | Attachment helpers |
| `annotations/` | Cross-cutting labels (`@Layer`) |

### Base URL resolution

Приоритет: `-DbaseUrl` / `baseUrl` в профиле → `-DbasePath` / `basePath`. Оба пустые — fail fast.

Local HTTP: сервер из **`frontend/`**, `baseUrl=http://localhost:3000/`. PO: `open("/login.html")`, `open("/header.html")` — без отдельных path keys.

**Один `gradle test` — один стенд** (`Configuration.baseUrl` в `@BeforeAll`). Smoke, screenshot baseline — отдельные прогоны / env-профили.

### Config keys (канон)

| Key | Default | Назначение |
|-----|---------|------------|
| `env` | `local` | Имя файла `config/${env}.properties` |
| `baseUrl` | `""` | HTTP(S) корень приложения |
| `basePath` | `""` | Локальная директория (file://) |
| `browser` | `chrome` | Браузер Selenide |
| `browserVersion` | `148` | Версия (remote) |
| `browserSize` | `1920x1280` | Размер окна |
| `headless` | `false` | Headless mode |
| `closeBrowserAfterEach` | `false` | Закрывать driver после теста |
| `remoteUrl` | `""` | Selenoid / Grid hub |
| `enableVnc`, `enableVideo`, `enableHar` | `false` | Selenoid capabilities (`enableVideo` — запись mp4 на hub) |
| `videoFolder` | `""` | Prefix URL для video attachment |
| `attachVideo` | `false` | Attachment в Allure; работает вместе с `enableVideo` |
| `allureReportMode` | `allure3` | `none` / `allure2` / `allure3` — слой Allure Report (см. `e2e-config-keys`) |
| `logToConsole` | `true` | Мастер stdout |
| `selenideLogToConsole` | `true` | Selenide SimpleReport |
| `rootLogLevel` | `info` | SLF4J simple default level |
| `enableAllureSelenideListener` | `false` | Auto-steps из Selenide в Allure |
| `attachBrowserConsoleLogs`, `attachPageSource`, `attachLastScreenshot`, `attachHarLogs`, `attachVideo` | `false` | Артефакты в `@AfterEach` |

**TestOps** (`ALLURE_ENDPOINT`, `ALLURE_TOKEN`, …) — env в CI, канон в `qa-guru-home/`; e2e-builder генерирует shell-блок, не `-D`.

Override: `-Dkey=value` или `-Denv=selenoid-local`.

## Паттерны (RAG)

Индекс: [`docs/rag/README.md`](../rag/README.md). Чанки: `docs/rag/e2e/<id>.md`.

| id | chunk |
|----|-------|
| `e2e-layers` | слои stack-agnostic |
| `e2e-config-keys` | таблица ключей (дубль § выше для retrieval) |
| `cfg-env-profile` | выбор env profile |
| `cfg-base-url` | resolve baseUrl / basePath |
| `base-lifecycle` | TestBase setup/teardown |
| `po-locators` | data-testid в PO |
| `po-fluent` | fluent chain |
| `po-step` | @Step в PO |
| `test-style-ladder` | учебная градация LoginTests |
| `test-taxonomy` | @Epic / @Tag |
| `test-negative` | negative validation |
| `test-storage-shortcut` | localStorage auth |
| `allure-attach` | opt-in artifacts |
| `allure-selenide-listener` | AllureSelenide global + per-test override |
| `remote-selenoid` | remote hub |
| `visual-baseline` | visual baselines (header, login, logged-in) |
| `test-pyramid` | testing pyramid — канон `tests-java/` |
| `test-manual` | exploratory vs TestOps `@Manual` |
| `test-logout-flow` | logout patterns (curriculum) |

## Split: канон vs curriculum

| | `tests-java/` | `tests-java-curriculum/` |
|---|---------------|--------------------------|
| Назначение | CI, bootstrap, pyramid | учебная ladder, e2e-builder presets |
| `LoginTests` | 1 smoke PO + `@Manual` exploratory | full style ladder + negative |
| `LogoutTests` | — | form + localStorage fluent |
| Manual | `@Manual` на методе в `LoginTests`/`HeaderTests` exploratory | `shortLoginAuthorizationTest` TestOps |

## Учебная градация в `LoginTests` (curriculum)

Код: `tests-java-curriculum/src/test/java/tests/LoginTests.java`. Smoke-метод дублируется в каноне.

| Тест | Стиль | Зачем |
|------|-------|-------|
| `shouldLoginWithValidCredentials` | Page Object | Канон smoke: fluent PO + assert |
| `wrongPasswordAuthorizationTest` | Raw inline `$()` | `stepsLocation=none`, listener global off |
| `emptyPasswordAuthorizationTest` | Inline + nested `Allure.step` | `test_allure_step`, `block_nested` |
| `emptyLoginAuthorizationTest` | Inline + `@EnableAllureSelenideListener` | `selenide_listener`, per-test ON, без ручных step |
| `emptyLoginAndPasswordAuthorizationTest` | Raw inline + `@EnableAllureSelenideListener(false)` | per-test listener OFF (явный override) |
| `shortLoginAuthorizationTest` | TestOps manual, только `Allure.step` | `@Manual`, `@AllureId`, без браузера |
| Закомментированный `successfulAuthorizationTest` | Raw Selenide | «До PO» — baseline для сравнения |

В **production** consumer-репо новые тесты — через PO; один источник шагов (PO `@Step` **или** listener **или** `Allure.step` в тесте). Смешение стилей в одном классе здесь — **curriculum**, не баг.

## Что не переносим как канон

- `harLogs()` — stub; включать `attachHarLogs` нельзя до реализации.
- Header visual baselines — не в scope 4a; общий паттерн — чанк `visual-baseline` (header + login + logged-in).

## Последствия

- Consumer bootstrap (`bootstrap-test-repo` skill) копирует структуру из `tests-java/README.md` + чанки `docs/rag/`.
- Header e2e — ADR `003-header-smoke-e2e.md` (layout probes + behavioral smoke).
- Visual baselines — cross-epic: `helpers/ScreenshotBaseline.java`, `@Tag("visual")`, флаг `updateBaselines`; чанк `visual-baseline`.
- `project-map.mdc` / `skills-map.md`: фазы 4a ✓, 4.pyramid ✓, 4b — по ADR 003.
