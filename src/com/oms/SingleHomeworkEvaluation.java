package com.oms;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SingleHomeworkEvaluation {

	static WebDriver driver;
	static ArrayList<String> tabs;
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://erp.letseduvate.com";
		
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
			
		    tabs = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(0)); //switches to new tab
			
		    performLogin();
			
			openCompletedClassesPage();
			
			viewSubmissions(2);
			
			Thread.sleep(2000);
//			skipPages(driver, 13);
			
//			checkAndGOToNextPage();
			
			Thread.sleep(5000);
			//closing the browser
			driver.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static void viewSubmissions(int skip) {
		try {
			
			String submissionLinkList = "//button[@title='View Submission']";
			List<WebElement> elements  = driver.findElements(By.xpath(submissionLinkList));
			System.out.println(elements.size());
			int totalSubmissions = elements.size();
			int skipped = skip;
			int periodsChecked = 0;
			while (skip >= totalSubmissions) {
				executeJSCode("scroll(0, 300)");
				Thread.sleep(2000);
				
				String nextButtonLink = "//button[text()='Next']";
				driver.findElement(By.xpath(nextButtonLink)).click();
				skip -= totalSubmissions;
			}
			elements  = driver.findElements(By.xpath(submissionLinkList));
			totalSubmissions = elements.size();
			for(int i =skip; i< totalSubmissions; i++) {
				System.out.println("i = " + i);
				Thread.sleep(1000);
				
				elements  = driver.findElements(By.xpath(submissionLinkList));
				elements.get(i).click();
				Thread.sleep(1000);
				
				String noDataLink = "//span[text()='No data Found']";
				try {
					WebElement noDataElement = driver.findElement(By.xpath(noDataLink));
					periodsChecked++;
					
					if(noDataElement !=null) {
						executeJSCode("history.back()");
						Thread.sleep(1000);
						String completedClassesLink = "//span[text()='Completed Classes']";
						driver.findElement(By.xpath(completedClassesLink)).click();
						Thread.sleep(2000);
						skipped++;
						skip = skipped;
						while (skip >= totalSubmissions) {
							executeJSCode("scroll(0, 300)");
							Thread.sleep(1000);
							
							String nextButtonLink = "//button[text()='Next']";
							driver.findElement(By.xpath(nextButtonLink)).click();
							skip -= totalSubmissions;
						}
						skip++;
						i=skip;
						continue;
					}
				}catch(NoSuchElementException e) {
					System.out.println(periodsChecked + " checked");
					String studentsHomeWorkLink = "//span[text()='View Submission']";
					List<WebElement> studentListElements  = driver.findElements(By.xpath(studentsHomeWorkLink));
					List<WebElement> pages;
					for(int j =0; j< studentListElements.size(); j++) {
						studentListElements.get(j).click();
						Thread.sleep(500);
						String evaluateImagesLink = "//span[text()='Evaluate Images']";
						driver.findElement(By.xpath(evaluateImagesLink)).click();
						
						tabs = new ArrayList<String> (driver.getWindowHandles());
						driver.switchTo().window(tabs.get(1));
						Thread.sleep(1000);
						pages= driver.findElements(By.xpath("//img"));
						Actions clickAt = new Actions(driver);
						
//						System.out.println(pages.size());
//						int count = pages.size()/2;
//						for(int k =0; k< count; k++) {
							
							clickAt.moveToElement(pages.get(0), 150, 200).click().build().perform();
							Thread.sleep(3000);
//							executeJSCode("scroll(0, " + ((k+1)*500)+ ")");
//						}
						String submitButtonLink = "//button[text()='FINISH']";
						//driver.findElement(By.xpath(submitButtonLink)).click();
						Thread.sleep(1000);
						String confirmSubmitButtonLink = "//span[text()='Yes']";
						//driver.findElement(By.xpath(confirmSubmitButtonLink)).click();
						String res = sc.next();
						//executeJSCode("window.close()");
						if(res.equals("y")) {
							driver.switchTo().window(tabs.get(0));
							
							String overAllReviewTextXPath = "//input[@type='text']";
							String review = "Read the lesson again.";
						//	driver.findElement(By.xpath(overAllReviewTextXPath)).sendKeys(review);
							
							Thread.sleep(1000);
							String submitReviewButton = "//span[text()='Submit Remarks']";
							driver.findElement(By.xpath(submitReviewButton)).click();
							
	//						Thread.sleep(1000);
	//						String backdropArea = "//div[@class='backdrop_backdrop__1WWzw']";
							Thread.sleep(5000);
						}
//						clickAt.moveToElement(driver.findElement(By.xpath(backdropArea)), 10, 10).click().build().perform();
//						driver.findElement(By.xpath(backdropArea)).click();
						
					}
					executeJSCode("history.back()");
				}
		    } 
			System.out.println(skipped + " " + periodsChecked);
			System.out.println(skipped+periodsChecked);
//			viewSubmissions(skipped+periodsChecked);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void executeJSCode(String code) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
	    js.executeScript(code);
	}

	private static void skipPages(int skip) {
		try {
			String nxtBtnXPath = "//button[@aria-label='Next Page']";
			while( skip > 1 ) {
				Thread.sleep(2000);
				driver.findElement(By.xpath(nxtBtnXPath)).click();
				skip--;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void checkAndGOToNextPage() {
		try {
			//scroll to top 
			JavascriptExecutor js = (JavascriptExecutor)driver;
			 // if the element is on top.
			
			String nxtBtnXPath = "//button[@aria-label='Next Page']";
			while(driver.findElement(By.xpath(nxtBtnXPath)) != null) {
				Thread.sleep(2000);
				js.executeScript("scroll(0, 0)");
				Thread.sleep(2000);
				driver.findElement(By.xpath(nxtBtnXPath)).click();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	private static void openCompletedClassesPage() {
		try {
			Thread.sleep(2000);
			
			String drawerButtonXpath = "//button[@aria-label='Open drawer']";
			driver.findElement(By.xpath(drawerButtonXpath)).click();
			Thread.sleep(2000);
			
			String blogsLink = "//li//p[text()='Online Class']";
			driver.findElement(By.xpath(blogsLink)).click();
			Thread.sleep(1000);
			
			String viewblogsLink = "//li[@path=\"/online_class/view_alloted_classes\"]";
			driver.findElement(By.xpath(viewblogsLink)).click();
			Thread.sleep(1000);
			
			String completedClassesLink = "//span[text()='Completed Classes']";
			driver.findElement(By.xpath(completedClassesLink)).click();
			Thread.sleep(1000);
			
			executeJSCode("scroll(0, 300)");
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void performLogin() {
		try {
			
			String username = "20190426217@orchids.edu.in";
			String password = "Ushagupta@070268";
			String loginXPath = "/html/body/div[@id='root']/main/div/form/button[@type='submit']"; 
			
			driver.findElement(By.id("username")).sendKeys(username);
//			Thread.sleep(500);
			
			driver.findElement(By.id("password")).sendKeys(password);
//			Thread.sleep(1000);
			
			driver.findElement(By.xpath(loginXPath)).click();
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}