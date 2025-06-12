package br.ifsp.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UITests extends BaseSeleniumTest{

    @Test
    @DisplayName("Should open register page")
    @Tag("UiTest")
    public void shouldOpenRegisterPage() {
        driver.get("http://localhost:3000/login");
        final WebElement registerButton =
                driver.findElement(By.xpath("//button[not(@type='submit')]"));
        delay(1000);
        registerButton.click();
        delay(1000);
    }
}
