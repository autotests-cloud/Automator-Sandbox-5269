---
id: test-pyramid
domain: e2e
phase: 4.pyramid
adr: 002
tags: [structure, junit, layer, pyramid]
---
# Testing pyramid (канон)

**id:** `test-pyramid`

Ярусы тестов в **`tests-java/`** — единственный канон для CI и bootstrap. Роль теста: `@Layer` + `@Tag` + `@Epic`. Имена классов **без** Smoke / Visual / Mount в названии.

## Файлы (канон)

| Layer | Путь / классы | Target |
|-------|---------------|--------|
| unit | `helpers/*Test`, `config/ConfigReaderTest` | pure Java |
| component | `tests/component/*Tests` | `/components.html` |
| integration | `HeaderLayoutTests`, `LoginFormTests`, `LoginEmbedTests` | `/header.html`, `/login.html` mount + embed |
| e2e | `HeaderTests`, `LoginTests`, `*BaselineTests` | harness + screenshot diff |
| manual | `@Manual` в `LoginTests`, `HeaderTests` | exploratory stubs (`@Tag("manual")` на методе) |

Login: `LoginFormTests` (integration) → `LoginTests` (e2e smoke) → `LoginBaselineTests` / `LoggedInBaselineTests` (visual).  
Header: `HeaderLayoutTests` (integration) → `HeaderTests` (e2e smoke) → `HeaderBaselineTests` (visual).

Учебная ladder-градация стилей — **`tests-java-curriculum/`**, чанк `test-style-ladder`. Не смешивать с каноном.

## Gradle

```bash
gradle test --tests 'helpers.*Test' --tests 'config.*Test'   # unit
gradle test -DincludeTags=component                          # component
gradle test -DincludeTags=layout,mount                       # integration
gradle test -DincludeTags=smoke -DexcludeTags=visual         # e2e smoke
gradle test -DincludeTags=visual                             # baselines
gradle test -DincludeTags=manual                             # exploratory manual
```

## Do

- Новый сценарий: выбрать ярус → один класс на concern (`LoginTests` = smoke + `@Manual` exploratory).
- Stack-слои (`config/`, `pages/`, `TestBase`) — чанк `e2e-layers`.

## Don't

- Возвращать curriculum ladder (negative inline, listener demo) в `tests-java/LoginTests`.
- Смешивать `@Tag("smoke")` и `@Tag("visual")` в одном `@Test`.
- Дублировать logout ladder в каноне — см. `test-logout-flow` (curriculum).
