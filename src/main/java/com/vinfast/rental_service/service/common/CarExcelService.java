package com.vinfast.rental_service.service.common;

import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.model.Car;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void exportToExcel(List<Car> cars, String filePath) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Cars");

            CellStyle headerStyle = createHeaderStyle(workbook);
            createHeaderRow(sheet, headerStyle);

            CellStyle dataStyle = createDataStyle(workbook);
            fillDataRows(sheet, dataStyle, cars);

            autoSizeColumns(sheet);

            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
                workbook.write(fileOut);
                log.info("Excel file has been written to: {}", filePath);
            }
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }

    private void createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);

        String[] headers = {
                "ID", "Biển số", "Số VIN", "Màu sắc",
                "Ngày sản xuất", "Số km hiện tại", "Trạng thái",
                "Lần bảo dưỡng cuối", "Km bảo dưỡng tiếp theo",
                "Tình trạng pin (%)", "Model xe", "Địa điểm nhận xe",
                "Ngày tạo", "Ngày cập nhật"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillDataRows(Sheet sheet, CellStyle dataStyle, List<Car> cars) {
        int rowNum = 1;

        for (Car car : cars) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;

            createCell(row, colNum++, car.getId(), dataStyle);

            createCell(row, colNum++, car.getLicensePlate(), dataStyle);

            createCell(row, colNum++, car.getVinNumber(), dataStyle);

            createCell(row, colNum++, car.getColor(), dataStyle);

            createCell(row, colNum++,
                    car.getManufacturingDate().format(DATE_FORMATTER),
                    dataStyle);

            createCell(row, colNum++, car.getCurrentMileage(), dataStyle);

            createCell(row, colNum++,
                    translateStatus(car.getStatus()),
                    dataStyle);

            createCell(row, colNum++,
                    car.getLastMaintenanceDate() != null ?
                            car.getLastMaintenanceDate().format(DATE_FORMATTER) : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getNextMaintenanceMileage() != null ?
                            car.getNextMaintenanceMileage().toString() : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getBatteryHealth() != null ?
                            car.getBatteryHealth().toString() : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getCarModel() != null ?
                            car.getCarModel().getName() : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getPickupLocation() != null ?
                            car.getPickupLocation().getName() : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getCreatedAt() != null ?
                            car.getCreatedAt().format(DATETIME_FORMATTER) : "",
                    dataStyle);

            createCell(row, colNum++,
                    car.getUpdatedAt() != null ?
                            car.getUpdatedAt().format(DATETIME_FORMATTER) : "",
                    dataStyle);
        }
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);

        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 14; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private String translateStatus(CarStatus status) {
        if (status == null) return "";

        switch (status) {
            case available: return "Sẵn sàng";
            case rented: return "Đang thuê";
            case maintenance: return "Bảo dưỡng";
            case unavailable: return "Không khả dụng";
            default: return status.toString();
        }
    }
}