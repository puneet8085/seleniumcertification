
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Selenium {
public WebDriver driver;
private Logger logger;
    @BeforeMethod
    public void setup() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "./driver/geckodriver.exe");
        driver= new FirefoxDriver();
        driver.get("https://www.google.com/intl/en-GB/gmail/about/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);

        //if you getting logger error that appender is not defined for class then add below line
        //BasicConfigurator.configure();
        logger=Logger.getLogger(this.getClass());
        logger.info("Current url is "+driver.getCurrentUrl());
        logger.info("Site opened successfully");
        driver.findElement(By.xpath("//a[contains(text(),'Create an account')]")).click();
        Thread.sleep(9000);
       for(String s :driver.getWindowHandles())
        driver.switchTo().window(s);

        logger.info("Current url is "+driver.getCurrentUrl());
        Thread.sleep(9000);
    }

    @Parameters({"firstname","lastname","username","password","confirmpassword"})
@Test
public void  FillDetialsUsingParameter(String firstname,String lastname,String username,String password,String confirmpassword)
{
    driver.findElement(By.id("firstName")).sendKeys(firstname);
    driver.findElement(By.id("lastName")).sendKeys(lastname);
    driver.findElement(By.id("username")).sendKeys(username);
    driver.findElement(By.name("Passwd")).sendKeys(password);
    driver.findElement(By.name("ConfirmPasswd")).sendKeys(confirmpassword);


}

    @DataProvider(name="signup")
    public Object[] []signUpDetails()
    {
        //Rows - Number of times your test has to be repeated.
        //Columns - Number of parameters in test data.
        Object[][] data = new Object[2][5];

        // 1st row
        data[0][0] ="fname1";
        data[0][1] = "lname1";
        data[0][2]="username1";
        data[0][3]="password1@";
        data[0][4]="password1@";


        // 2nd row
        data[1][0] ="fname2";
        data[1][1] = "lname2";
        data[1][2]="username2";
        data[1][3]="password2@";
        data[1][4]="password2@";
        return data;
    }

    @Test(dataProvider = "signup")
    public void  FillDetialsUsingDataProvider(String firstname,String lastname,String username,String password,String confirmpassword)
    {
        driver.findElement(By.id("firstName")).sendKeys(firstname);
        driver.findElement(By.id("lastName")).sendKeys(lastname);
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.name("Passwd")).sendKeys(password);
        driver.findElement(By.name("ConfirmPasswd")).sendKeys(confirmpassword);


    }


//fill data from excel and write in excel another sheet
    @Test()
    public void FIllDetailsFromExcelAndWriteInExcel() throws IOException {

        //if you are getting error in reading data due to given excel format then change XSSFWorkbook, XSSFSheet with HSSFworkbook and HSSFSheet.

        FileInputStream fis = new FileInputStream("e:\\Test_Data.xlsx");
        XSSFWorkbook workbook= new XSSFWorkbook(fis);
        XSSFSheet sheet= workbook.getSheet("Sheet1");

    String firstname=sheet.getRow(1).getCell(0).getStringCellValue();
    String lastname=sheet.getRow(1).getCell(1).getStringCellValue();
    String username=sheet.getRow(1).getCell(2).getStringCellValue();
    String password=sheet.getRow(1).getCell(3).getStringCellValue();
    String confirmpassword=sheet.getRow(1).getCell(4).getStringCellValue();


    driver.findElement(By.id("firstName")).sendKeys(firstname);
    driver.findElement(By.id("lastName")).sendKeys(lastname);
    driver.findElement(By.id("username")).sendKeys(username);
    driver.findElement(By.name("Passwd")).sendKeys(password);
    driver.findElement(By.name("ConfirmPasswd")).sendKeys(confirmpassword);

    String fname=driver.findElement(By.id("firstName")).getAttribute("data-initial-value");
    String lname= driver.findElement(By.id("lastName")).getAttribute("data-initial-value");

    XSSFSheet newsheet= workbook.createSheet("Sheet2");
    newsheet.createRow(0).createCell(0).setCellValue(fname);
    newsheet.createRow(0).createCell(1).setCellValue(lname);
    FileOutputStream fos = new FileOutputStream("e:\\Test_Data.xlsx");
    workbook.write(fos);
    fos.close();


    }

    public void screenShot() throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        FileUtils.copyFile(source,new File("./screenshot/"+logger.getName()+"screenshot.png"));

    }
    @AfterMethod
    public void teardown() throws IOException {
        screenShot();
        driver.quit();
    }

}
