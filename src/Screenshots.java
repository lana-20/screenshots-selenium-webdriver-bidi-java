import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.browsingcontext.BrowsingContext;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebElement;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class Screenshots { 

	static protected WebDriver driver;

	public static void main(String[] args) {
		chromedriver().setup();
		var options = new ChromeOptions();
		options.enableBiDi();
		WebDriver driver = new ChromeDriver(options);

		driver.get("https://the-internet.herokuapp.com/challenging_dom");
		var browsingContext = new BrowsingContext(driver, driver.getWindowHandle());

		// full page
		String fullScreenshot = browsingContext.captureScreenshot();
		saveScreenshot(fullScreenshot, "full_screenshot.png");

		// element
		WebElement canvas = driver.findElement(By.cssSelector("#canvas"));
		String internalElementId = ((RemoteWebElement) canvas).getId();
		String elementScreenshot = browsingContext.captureElementScreenshot(internalElementId);
		saveScreenshot(elementScreenshot, "element_screenshot.png");

		// viewport
//		driver.findElement(By.name("my-date")).click();
		var largeColumn = driver.findElement(By.className("large-2")).getRect();
		String viewportScreenshot = browsingContext.captureBoxScreenshot(
				largeColumn.getX() - 30,
				largeColumn.getY() - 30,
				largeColumn.getWidth() + 30,
				largeColumn.getHeight() + 30
				);

		saveScreenshot(viewportScreenshot, "viewport_screenshot.png");

		driver.quit();
	}

	private static void saveScreenshot(String screenshot, String filename) {
		var decodedScreenshot = Base64.getDecoder().decode(screenshot);
		try {
			String path = "/Users/lanabegunova/eclipse-workspace/TakingScreenshotsBiDi/src/screenshots/";
			Files.write(Paths.get(path + filename), decodedScreenshot);
		} catch (IOException e) {
			e.printStackTrace();
   }
 }

}