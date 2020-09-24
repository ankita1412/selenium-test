package com.oms;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class EvaluateBlogs {

	static String urlCheckPlagiarism = "https://www.grammarly.com/plagiarism-checker?q=plagiarism&utm_source=google&utm_medium=cpc&utm_campaign=searchunitedstateslocalization-PL-e&utm_content=394560095665&utm_term=anti%20plagiarism%20checker&matchtype=e&placement=&network=g&gclid=EAIaIQobChMIpvDo-bLS6gIVgZ6zCh0o4QgVEAAYASAAEgLLdvD_BwE";
	static WebDriver driver;
	
	enum REVIEW_TYPE{
		COPIED_CONTENT,
		LESS_CONTENT,
		AVG_CONTENT
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "https://erp.letseduvate.com/blog/view/reviewer";
		
		try {
			//setting the driver executable
			System.setProperty("webdriver.chrome.driver", "/Users/angupta/Documents/SW/chromedriver 3");
			
			//Initiating your chromedriver
			driver=new ChromeDriver();
			
			//Applied wait time
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//			//maximize window
//			driver.manage().window().maximize();
//			
			//open browser with desried URL
			driver.get(url);
			
//			executeJSCode("window.open(\"" + urlCheckPlagiarism + "\")");
		    
//		    ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
//		    driver.switchTo().window(tabs.get(0)); //switches to new tab
			
		    performLogin();
			
			openBlogsPage();
			Thread.sleep(2000);
			processBlogs(3);
//			skipPages(driver, 13);
			
			checkAndGOToNextPage();
			
			Thread.sleep(5000);
			//closing the browser
			driver.close();
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
				Thread.sleep(1000);
				processBlogs(1);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void processBlogs(int skip) {
		try {
			String blogListXPath = ".blog_tab .MuiGrid-item";
			List<WebElement> elements  = driver.findElements(By.cssSelector(blogListXPath));
			executeJSCode("scroll(0, 600)");
			Thread.sleep(3000);
			
	    	for(int i =skip-1; i< elements.size(); i++) {
	    		elements  = driver.findElements(By.cssSelector(blogListXPath));
	    		if(i == elements.size()) {
	    			break;
	    		}
	    		if(i > 5) {
	    			executeJSCode("scroll(0, 1800)");
	    			Thread.sleep(2000);
	    		}	
		    	WebElement element = elements.get(i);
		    	element.findElement(By.xpath("(.//button)[2]")).click();
		    	boolean blogReviewed = evaluateBlog();
		    	Thread.sleep(2000);
		    	if(blogReviewed) {
		    		i--;
		    	};
		    	
//		    	if(i == 0) {
//		    		break;
//		    	}
		    } 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static boolean evaluateBlog() {
		try {
			System.out.println();
			Thread.sleep(1000);
			String titleXPath= "//div[contains(@class,'MuiDialog-paper')]/p[contains(@class,\"MuiTypography-body1\")][1]";
			String mainTextXPath= "//div[contains(@class,'MuiDialog-paper')]/p[contains(@class,\"MuiTypography-body1\")][3]";
			
			System.out.println(driver.findElement(By.xpath(titleXPath)).getText());

			String content = driver.findElements(By.xpath(mainTextXPath)).get(0).getText();
			
			int numWords = content.split(" ").length;
			System.out.println(numWords);
			if(numWords <= 100) {
				System.out.println("Content too less");
				reviewBlog(REVIEW_TYPE.LESS_CONTENT);
				return true;
				
			} else {
//				reviewBlog(driver);
//				if(checkForPlagiarism(content)) {
//					reviewBlog(REVIEW_TYPE.COPIED_CONTENT);
//					return true;
//				} else {
					reviewBlog(REVIEW_TYPE.AVG_CONTENT);
					System.out.println("Content reviewed as avg");
//					Thread.sleep(1000);
//					String closeButtonXPath = "//button[@aria-label='close']";
//					driver.findElement(By.xpath(closeButtonXPath)).click();
//				}
				Thread.sleep(1000);
				return true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean checkForPlagiarism(String content) {
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		try {

		    driver.switchTo().window(tabs.get(1)); //switches to new tab
		    
		    String textAreaPath = "//textarea[@data-qa=\"textarea\"]";
//		    System.out.println(content);
		    String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
		    content = content.replaceAll(characterFilter,"");
		    driver.findElement(By.xpath(textAreaPath)).sendKeys(content);
		    
		    String checkBtnXPath = "//div[@data-qa=\"btnCheckText\"]";
		    driver.findElement(By.xpath(checkBtnXPath)).click();
		    
		    Thread.sleep(content.length()*10);
		    String copiedContentDivXPath= "//div[contains(text(),'lagiarism was detected')]";
		    if(driver.findElement(By.xpath(copiedContentDivXPath)) != null) {
		    	System.out.println("Copied content");
		    	reloadCheckPliagiarismPage();
		    	driver.switchTo().window(tabs.get(0)); //switches to new tab
		    	return true;
		 	}
		    return false;
			 
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			String originalContentDivXPath = "//div[text()='Plagiarism was not detected']";
			if(driver.findElement(By.xpath(originalContentDivXPath)) != null){
				System.out.println("original Content");
				reloadCheckPliagiarismPage();
				driver.switchTo().window(tabs.get(0)); //switches to new tab
			} else {
				System.out.println("Page not loaded");
				e.printStackTrace();
			}
	    	return false;
		}
		return false;
	}

	private static void reloadCheckPliagiarismPage() {
		executeJSCode("location.href = \"" + urlCheckPlagiarism +"\"");
	}

	private static void reviewBlog(REVIEW_TYPE type) {
		String review; 
		
		try {
			switch (type) {
			case AVG_CONTENT: {
//				review = "Copied content.. Blog should be written by original thoughts and opinion.. you can take reference from different sources though.";
				review = "Good content.. Take care of the following things - Title should be catchy.. blog should contain your personal experience in structured way (Intro.. content… conclusion) and should explain certain findings from your opinion.. can include pictures, links ..so that it seem interesting for the reader.";
				break;
			}
			case LESS_CONTENT: {
				review = "Too less content.. Title should be catchy.. blog should contain your personal experience in structured way (Intro.. content… conclusion) and should explain certain findings from your opinion.. can include pictures, links ..so that it seem interesting for the reader.";
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + type);
			}
//			String lessContentReview = "Too less content.. Title should be catchy.. blog should contain your personal experience in structured way (Intro.. content… conclusion) and should explain certain findings from your opinion.. can include pictures, links ..so that it seem interesting for the reader.";
			
			Thread.sleep(1000);
			String reviewButtonXPath = "//button[text()='Add Review']";
			driver.findElement(By.xpath(reviewButtonXPath)).click();
			
			Thread.sleep(2000);
			String ratingStarXPath = "//fieldset[contains(@class,'MuiBox-root')]";
			List<WebElement> elements  = driver.findElements(By.xpath(ratingStarXPath));
			
			ListIterator<WebElement> iterator = elements.listIterator();
			String ratingXPath;
			if(type.equals(REVIEW_TYPE.AVG_CONTENT))
				ratingXPath= "(.//span[contains(@class,'MuiRating-decimal')])[3]"; //".//input[@value='2']";
			else 
				ratingXPath= "(.//span[contains(@class,'MuiRating-decimal')])[2]"; //".//input[@value='2']";
			 while (iterator.hasNext()) {
				 System.out.println();
		    	WebElement element = iterator.next();
		    	element.findElement(By.xpath(ratingXPath)).click();
		    	if(iterator.previousIndex() == 5)
		    		break;
//		    	else if(iterator.previousIndex() == 10 && type.equals(REVIEW_TYPE.AVG_CONTENT))
//		    		break;
			 }
			 String overAllReviewTextXPath = "//textarea[@placeholder='Add review...(Mandatory)']";
			 driver.findElement(By.xpath(overAllReviewTextXPath)).sendKeys(review);
			 
			 Thread.sleep(1000);
			 
			 String submitButtonXPath = "//button//span[text()='Submit']";
			 driver.findElement(By.xpath(submitButtonXPath)).click();

			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void openBlogsPage() {
		try {
			Thread.sleep(2000);
			
			String drawerButtonXpath = "//button[@aria-label='Open drawer']";
			driver.findElement(By.xpath(drawerButtonXpath)).click();
			Thread.sleep(2000);
			
			String blogsLink = "//li//p[text()='Blogs']";
			driver.findElement(By.xpath(blogsLink)).click();
			Thread.sleep(1000);
			
			String viewblogsLink = "//li[@path=\"/blog/view/reviewer\"]";
			driver.findElement(By.xpath(viewblogsLink)).click();
			Thread.sleep(1000);
			
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