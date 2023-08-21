package ru.netology.card;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    @CsvFileSource(resources = "/valid.csv")
    public void shouldGetValidResults(String name, String phone, String expected) { //форма заполняется валидными данными
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidForName.csv", delimiter = '|')
    public  void shouldNotGetInvalidResultsForName(String name, String phone, String expected) { //невалидные значения для имени
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidForPhone.csv", delimiter = '|')
    public  void shouldNotGetInvalidResultsForPhone(String name, String phone, String expected) { //невалидные значения для телефона
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals(expected, actual);

    }

    @Test
    public void shouldNotGetWithoutName() { //поле "имя" не заполнено
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim();

        assertEquals("Поле обязательно для заполнения", actual);

    }

    @Test
    public void shouldNotGetWithoutPhone() { //поле "телефон" не заполнено
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Белов Ярослав");
        driver.findElement(By.cssSelector("[data-test-id=agreement")).click();
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim();

        assertEquals("Поле обязательно для заполнения", actual);

    }

    @Test
    public void shouldNotGetWithoutAccept() { //не поставлена галочка
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Белов Ярослав");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.className("button")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim();

        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", actual);

    }

}
