package com.example.excel;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@UtilityClass
public class ExcelUtils {
    public void write(Path excelFile, ExcelSheetIterator first, ExcelSheetIterator... rest) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            writeWorkbook(workbook, first);
            for (ExcelSheetIterator excelSheetIterator : rest) {
                writeWorkbook(workbook, excelSheetIterator);
            }
            try (OutputStream fos = Files.newOutputStream(excelFile)) {
                workbook.write(fos);
            }
            workbook.dispose();
        }
    }

    private void writeWorkbook(SXSSFWorkbook workbook, ExcelSheetIterator sheetIterator) {
        SXSSFSheet sheet = StringUtils.isBlank(sheetIterator.getSheetName()) ? workbook.createSheet() : workbook.createSheet(sheetIterator.getSheetName());
        int rowIndex = 0;
        while (sheetIterator.hasNext()) {
            List<String> data = sheetIterator.next();
            SXSSFRow row = sheet.createRow(rowIndex++);
            for (int i = 1; i < data.size(); i++) {
                row.createCell(i)
                   .setCellValue(data.get(i));
            }
        }
    }
}
