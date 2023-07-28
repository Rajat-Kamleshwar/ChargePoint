import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlipkartTest {
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static final String url = "https://www.flipkart.com/";

    public static void main(String[] args) {
        invokeBrowser();
        searchItem();
        trendingItems();
        bestSellerItems();
        closeBrowser();
    }

    static void invokeBrowser(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(url);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            if (driver.findElement(By.xpath("//span[text()='Login']/parent::span/parent::div/parent::div/parent::div")).isDisplayed()){
                WebElement popUpModal = driver.findElement(By.xpath("//span[text()='Login']/parent::span/parent::div/parent::div/parent::div"));
                WebElement crossBtn = driver.findElement(By.xpath("//button[text()='✕']"));
                try {
                    wait.until(ExpectedConditions.visibilityOf(popUpModal));
                } catch (NoSuchElementException e){
                    System.out.println("Exception:\t" + e);
                }
                try {
                    wait.until(ExpectedConditions.visibilityOf(crossBtn)).click();
                } catch (NoSuchElementException e){
                    System.out.println("Exception:\t" + e);
                }
            }
        } catch (NoSuchElementException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Proceeding Without Pop-Up");
        }
    }

    static void searchItem(){
        WebElement searchBar = driver.findElement(By.xpath("//input[@name='q']"));
        searchBar.sendKeys("boat blue");
        List<WebElement> searchSuggestions = driver.findElements(By.xpath("//ul/li"));
        try{
            wait.until(ExpectedConditions.visibilityOfAllElements(searchSuggestions));
        } catch (NoSuchElementException e){
            System.out.println("Exception:\t" + e);
        }
        for (WebElement w : searchSuggestions){
            if (w.getText().contains("earbuds")){
                w.click();
                break;
            }
        }
    }

    static void trendingItems(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try{
            Thread.sleep(5000);
        } catch (InterruptedException i){
            System.out.println(i);
        }
        List<WebElement> items = driver.findElements(By.xpath("//div[@id='container']/div/div[3]/div/div[2]/div/div/div/div/a[1]/div[2]/div"));
        try{
            wait.until(ExpectedConditions.visibilityOfAllElements(items));
        } catch (NoSuchElementException e){
            System.out.println("Exception:\t" + e);
        }

        for (WebElement e : items){
            if(e.getText().contains("Trending")){
                js.executeScript("arguments[0].scrollIntoView();", e);
                WebElement trendingItemDesc = driver.findElement(By.xpath("//div[text()='Trending']/parent::div/parent::a/following-sibling::a[1]"));
                String itemDesc = trendingItemDesc.getText();
                System.out.println("Trending Item Description:\t" + itemDesc);
                List<WebElement> trendingItemPrices = driver.findElements(By.xpath("//div[text()='Trending']/parent::div/parent::a/following-sibling::a[2]/div/div"));

                // Extract Prices from the Elements
                HashMap<String, String> map = new HashMap<>();
                map.put("Trending Item Selling Price", trendingItemPrices.get(0).getText().replaceAll("[^0-9]", ""));
                map.put("Trending Item Actual Price", trendingItemPrices.get(1).getText().replaceAll("[^0-9]", ""));
                map.put("Trending Item Discount", trendingItemPrices.get(2).getText().replaceAll("[^0-9]", ""));

                //Print the Prices
                for (Map.Entry<String, String> m : map.entrySet()){
                    System.out.println(m.getKey() + "\t" + m.getValue());
                }
            }
        }
    }

    static void bestSellerItems(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//input[@name='q']")));
        List<WebElement> items = driver.findElements(By.xpath("//div[@id='container']/div/div[3]/div/div[2]/div/div/div/div/a[1]"));
        try{
            wait.until(ExpectedConditions.visibilityOfAllElements(items));
        } catch (NoSuchElementException e){
            System.out.println("Exception:\t" + e);
        }

        for (WebElement e : items){
            if(e.getText().contains("Bestseller")){
                js.executeScript("arguments[0].scrollIntoView();", e);
                WebElement bestSellItemDesc = driver.findElement(By.xpath("//div[text()='Bestseller']/parent::div/parent::a/following-sibling::a[1]"));
                String itemDesc = bestSellItemDesc.getText();
                System.out.println("Best Seller Item Description:\t" + itemDesc);
                List<WebElement> bestSellItemPrices = driver.findElements(By.xpath("//div[text()='Bestseller']/parent::div/parent::a/following-sibling::a[2]/div[1]/div"));
                // Extract Prices from the Elements
                HashMap<String, String> map = new HashMap<>();
                map.put("Best Seller Item Selling Price", bestSellItemPrices.get(0).getText().replaceAll("[^0-9]", ""));
                map.put("Best Seller Item Actual Price", bestSellItemPrices.get(1).getText().replaceAll("[^0-9]", ""));
                map.put("Best Seller Item Discount Offer", bestSellItemPrices.get(2).getText().replaceAll("[^0-9]", ""));

                // Print the Prices
                for (Map.Entry<String, String> m : map.entrySet()){
                    System.out.println(m.getKey() + "\t" + m.getValue());
                }
            }
        }
    }

    static void closeBrowser(){
        driver.quit();
    }
}
