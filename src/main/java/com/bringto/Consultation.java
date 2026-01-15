package com.bringto;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.opencsv.CSVWriter;
import selenium.DriverFactory;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Consultation {
    public static void main(String[] args) {
        WebDriver driver = null;

        try {
            driver = DriverFactory.getDriver();
            driver.get("https://ayltoninacio.com.br/blog/tabela-cores-html-css-hexadecimal-nome-rgb");

            WebDriverWait wait = new WebDriverWait(driver, 10);

            WebElement tableColors = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[1]")));

            List<WebElement> rowsFromTable = tableColors.findElements(By.xpath(".//tr"));

            try (Writer fileOutput = new OutputStreamWriter(new FileOutputStream("table.csv"), StandardCharsets.UTF_8);
                 CSVWriter writerCSV = new CSVWriter(fileOutput)) {

                String[] header = {" | Color Name | Hexadecimal Code | RGB code | "};
                writerCSV.writeNext(header);

                for (int i = 1; i < rowsFromTable.size(); i++) {
                    WebElement line = rowsFromTable.get(i);
                    List<WebElement> columns = line.findElements(By.xpath(".//td"));
                    if (columns.size() < 3) continue;

                    String nameColor = columns.get(0).getText().trim();
                    String codeHex = columns.get(1).getText().trim();
                    String codeRGB = columns.get(2).getText().trim();

                    String[] dataLine = {nameColor, codeHex, codeRGB};
                    writerCSV.writeNext(dataLine);
                }
                System.out.println("Arquivo CSV criado com sucesso!");
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro durante a execução:");
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
