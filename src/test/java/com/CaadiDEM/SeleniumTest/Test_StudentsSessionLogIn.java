/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CaadiDEM.SeleniumTest;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author frodo
 */
public class Test_StudentsSessionLogIn {
    
    @Test
    public void testSimple() throws Exception {
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver/geckodriver");
        
         String path = "http://localhost:8080/ProyectoCaadiDEM/Visitas/LogInSession.xhtml";
         WebDriver d = new FirefoxDriver();
                d.get(path);

              
                
                WebElement inptxtNua = d.findElement(By.id("input_form:NUA"));
                WebElement btnSubmit = d.findElement(By.id("form:submit"));
                
                inptxtNua.sendKeys("914375");

                btnSubmit.submit();
                
               

       
    }
    
}
