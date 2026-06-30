---
id: remote-selenoid
domain: e2e
phase: 4a
adr: 002
tags: [selenide, junit, allure]
---
# CI remote browser

**id:** `remote-selenoid`

## Файлы

`selenoid-*.properties`

## Входы

remoteUrl

## Assert

Session on hub

## Do

selenoid:options map

## Don't

Local chrome flags на remote

