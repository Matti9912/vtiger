package hardcodedTestScripts;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class CreateEventTest {

	public static void main(String[] args) {
		Random random = new Random();
		int randomNum = random.nextInt(100);

		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8888/");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		if (driver.getTitle().contains("vtiger"))
			System.out.println("Login page displayed");
		else
			System.out.println("Login page not found");

		driver.findElement(By.name("user_name")).sendKeys("admin");
		driver.findElement(By.name("user_password")).sendKeys("admin");
		driver.findElement(By.id("submitButton")).submit();

		if (driver.getTitle().contains("Home"))
			System.out.println("Home page is displayed");
		else
			System.out.println("Home page not found");

		WebElement quickCreate = driver.findElement(By.id("qccombo"));
		Select s = new Select(quickCreate);
		s.selectByValue("Events");

		String header = driver.findElement(By.xpath("//td[@class='mailSubHeader']/b")).getText();
		if (header.contains("Create To Do"))
			System.out.println("Create To Do is displayed");
		else
			System.out.println("Create To Do not found");

		String subject = "Event" + randomNum;
		driver.findElement(By.name("subject")).sendKeys(subject);
		driver.findElement(By.id("jscal_trigger_date_start")).click();

		String currentMonthYear = driver
				.findElement(By
						.xpath("//div[@class='calendar' and contains(@style,'block')]/descendant::td[@class='title']"))
				.getText();

		String[] str = currentMonthYear.split(",");

		int currentMonthInNum = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH).parse(str[0])
				.get(ChronoField.MONTH_OF_YEAR);
		int currentYearInNum = Integer.parseInt(str[1].trim());
		int requiredYear = 2025;
		int requiredMonth = 10;
		int requiredDate = 18;

		while (currentYearInNum < requiredYear) {
			driver.findElement(By.xpath("//div[@class='calendar' and contains(@style,'block')]/descendant::td[.='»']"))
					.click();
			currentMonthYear = driver
					.findElement(By.xpath(
							"//div[@class='calendar' and contains(@style,'block')]/descendant::td[@class='title']"))
					.getText();
			str = currentMonthYear.split(",");
			currentMonthInNum = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH).parse(str[0])
					.get(ChronoField.MONTH_OF_YEAR);
			currentYearInNum = Integer.parseInt(str[1].trim());

			if (currentYearInNum == requiredYear) {
				while (currentMonthInNum < requiredMonth) {
					driver.findElement(
							By.xpath("//div[@class='calendar' and contains(@style,'block')]/descendant::td[.='›']"))
							.click();
					currentMonthYear = driver.findElement(By.xpath(
							"//div[@class='calendar' and contains(@style,'block')]/descendant::td[@class='title']"))
							.getText();
					str = currentMonthYear.split(",");
					currentMonthInNum = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH).parse(str[0])
							.get(ChronoField.MONTH_OF_YEAR);
				}
				while (currentMonthInNum > requiredMonth) {
					driver.findElement(
							By.xpath("//div[@class='calendar' and contains(@style,'block')]/descendant::td[.='‹']"))
							.click();
					currentMonthYear = driver.findElement(By.xpath(
							"//div[@class='calendar' and contains(@style,'block')]/descendant::td[@class='title']"))
							.getText();
					str = currentMonthYear.split(",");
					currentMonthInNum = DateTimeFormatter.ofPattern("MMMM").withLocale(Locale.ENGLISH).parse(str[0])
							.get(ChronoField.MONTH_OF_YEAR);
				}
			}
		}

		driver.findElement(By.xpath(
				"//div[@class='calendar' and contains(@style,'block')]/descendant::td[.='" + requiredDate + "']"))
				.click();
		
		WebElement dueDate = driver.findElement(By.id("jscal_field_due_date"));
		dueDate.clear();
		dueDate.sendKeys("2025-11-26");
		driver.findElement(By.xpath("//input[@value='  Save']")).click();
		String eventInfoPageHeader = driver.findElement(By.xpath("//span[@class='lvtHeaderText']")).getText();
		if(eventInfoPageHeader.contains(subject))
			System.out.println("Pass");
		else
			System.out.println("Fail");

		WebElement administratorIcon = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		Actions a = new Actions(driver);
		a.moveToElement(administratorIcon).perform();
		
		driver.findElement(By.xpath("//a[text()='Sign Out']")).click();
		driver.quit();

	}

}
