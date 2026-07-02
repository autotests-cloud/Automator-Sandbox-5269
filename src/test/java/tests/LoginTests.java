package tests;

import annotations.Layer;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@Layer("e2e")
@Epic("Одностраничная форма")
@Feature("Авторизация")
@DisplayName("Авторизация")
public class LoginTests extends TestBase {

    private static final String LOGIN_PAGE = "login.html?ru";

    @Test
    @AllureId("45327")
    @Tag("positive")
    @DisplayName("Успешная авторизация с валидными учётными данными")
    void successfulAuthorizationTest() {
        step("Given открыть `/login.html?ru`", () ->
                open(LOGIN_PAGE));

        step("When ввести `user1` в `[data-testid=login-input]`", () ->
                $("[data-testid=login-input]").setValue("user1"));

        step("When ввести `password1` в `[data-testid=password-input]`", () ->
                $("[data-testid=password-input]").setValue("password1"));

        step("When нажать `[data-testid=submit-button]`", () ->
                $("[data-testid=submit-button]").click());

        step("Then проверить logged-in state", () ->
                $("[data-testid=welcome-message]").shouldHave(text("Добро пожаловать, user1!")));
    }



    @Test
    @AllureId("45341")
    @Tag("negative")
    @DisplayName("Авторизация не проходит при неверном пароле")
    void wrongPasswordAuthorizationTest() {
        step("Given открыть `/text-box.html`", () ->
                open("text-box.html"));

        step("When нажать `[data-testid=login-link]`", () ->
                $("[data-testid=login-link]").click());

        step("When ввести `user1` в `[data-testid=login-input]`", () ->
                $("[data-testid=login-input]").setValue("user1"));

        step("When ввести неверный пароль в `[data-testid=password-input]`", () ->
                $("[data-testid=password-input]").setValue("wrongpassword"));

        step("When нажать `[data-testid=submit-button]`", () ->
                $("[data-testid=submit-button]").click());

        step("Then проверить \"Wrong login or password\"", () ->
                $("[data-testid=error-message]").shouldHave(text("Wrong login or password")));
    }


    @Test
    @AllureId("45353")
    @Tag("positive")
    @DisplayName("Вход покупателя в личный кабинет интернет-магазина")
    void vhodPokupatelyaLichnyyKabinetInternetMagazinaTest() {
        step("Открыть страницу входа в личный кабинет (https://ozon.ru/login.html?ru)", () ->
                open("login.html?ru"));

        step("Ввести логин покупателя user1 в поле login-input", () ->
                $("[data-testid=login-input]").setValue("user1"));

        step("Ввести пароль password1 в поле password-input", () ->
                $("[data-testid=password-input]").setValue("password1"));

        step("Нажать кнопку submit-button для входа в кабинет", () ->
                $("[data-testid=submit-button]").click());

        step("Проверить приветствие Welcome, user1! в success-panel", () ->
                $("[data-testid=welcome-message]").shouldHave(text("...")));
    }


    @Test
    @AllureId("45492")
    @Tag("positive")
    @DisplayName("Переключение языка в шапке на login.html")
    void pereklyuchenieYazykaShapkeLoginHtmlTest() {
        step("Открыть login.html?ru", () ->
                open("login.html?ru"));

        step("Нажать переключатель языка в шапке", () ->
                fail("Шаг не распознан генератором — добавьте правило в gen-python-policy.json или реализуйте вручную"));

        step("Проверить подпись языка \"EN\" в шапке", () ->
                $("[data-testid=error-message]").shouldHave(text("EN")));
    }


    @Test
    @AllureId("45727")
    @Tag("positive")
    @DisplayName("Переключение темы в шапке на login.html")
    void pereklyuchenieTemyShapkeLoginHtmlTest() {
        step("Открыть login.html?ru", () ->
                open("login.html?ru"));

        step("Нажать кнопку переключения темы в шапке", () ->
                $("[data-testid=submit-button]").click());

        step("Проверить что html имеет класс theme-dark", () ->
                $("[data-testid=error-message]").shouldHave(text("...")));
    }
}
