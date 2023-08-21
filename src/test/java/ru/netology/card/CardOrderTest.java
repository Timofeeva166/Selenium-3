package ru.netology.card;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @ParameterizedTest
    @CsvFileSource(files="src/test/resources/valid.csv")
    public void shouldGetValidResults(String name, String phone, String expected) { //форма заполняется валидными данными
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

}
