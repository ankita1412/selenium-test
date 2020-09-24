package com.oms;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class HomeworkEvaluation {

	static WebDriver driver;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://erp.letseduvate.com/qbox/evaluation_tool/?hw_submission_id=2747955,2750150";
		
		try {
			//setting the driver executable
			System.setProperty("webdriver.chrome.driver", "/Users/angupta/Documents/SW/chromedriver 3");
			
			//Initiating your chromedriver
			driver=new ChromeDriver();
			
			//Applied wait time
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//			//maximize window
			driver.manage().window().maximize();
//			
			//open browser with desried URL
			driver.get(url);
			
		    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(0)); //switches to new tab
			
		    
		    evaluateHomeWork();
		    
		    
			Thread.sleep(1000);
			//closing the browser
//			driver.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static void evaluateHomeWork() {
		try {
			List<WebElement> pages= driver.findElements(By.xpath("//img"));
			Actions clickAt = new Actions(driver);
			
			System.out.println(pages.size());
			int count = pages.size()/2;
			for(int k =0; k< count; k++) {
				
				clickAt.moveToElement(pages.get(k), 200, 100).click().build().perform();
				Thread.sleep(3000);
//				executeJSCode("scroll(0, " + ((k+1)*500)+ ")");
			}
			String submitButtonLink = "//button[text()='FINISH']";
			driver.findElement(By.xpath(submitButtonLink)).click();
			Thread.sleep(1000);
			String confirmSubmitButtonLink = "//span[text()='Yes']";
			driver.findElement(By.xpath(confirmSubmitButtonLink)).click();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void executeJSCode(String code) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
	    js.executeScript(code);
	}

}