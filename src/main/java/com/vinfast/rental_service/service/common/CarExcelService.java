package com.vinfast.rental_service.service.common;

import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.model.Car;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

//  Export
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

//    Import

    public List<Car> importFromExcel(MultipartFile file) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (InputStream in = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(in)) {

            Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();


            for (int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Car car = new Car();
                String licensePlate = getCellValue(row.getCell(1));
                String vinNumber = getCellValue(row.getCell(2));
                String color = getCellValue(row.getCell(3));

                String manufacturingDateStr = getCellValue(row.getCell(4));
                LocalDate manufacturingDate = manufacturingDateStr.isEmpty() ? null :
                        LocalDate.parse(manufacturingDateStr, DATE_FORMATTER);

                int currentMileage = (int) getNumericCellValue(row.getCell(5));



                String statusStr = getCellValue(row.getCell(6));
                CarStatus status = translateStatusFromString(statusStr);

                String lastMaintenanceDateStr = getCellValue(row.getCell(7));
                LocalDate lastMaintenanceDate = lastMaintenanceDateStr.isEmpty() ? null :
                        LocalDate.parse(lastMaintenanceDateStr, DATE_FORMATTER);

                double nextMaintenanceMileageDouble = getNumericCellValue(row.getCell(8));
                Integer nextMaintenanceMileage = (int) nextMaintenanceMileageDouble;


                double batteryHealthStr = getNumericCellValue(row.getCell(9));
                BigDecimal batteryHealth = BigDecimal.valueOf(batteryHealthStr);


                String carModelName = getCellValue(row.getCell(10));
                String pickupLocationName = getCellValue(row.getCell(11));

                String createdAtStr = getCellValue(row.getCell(12));
                LocalDateTime createdAt = createdAtStr.isEmpty() ? null :
                        LocalDateTime.parse(createdAtStr, DATETIME_FORMATTER);

                String updatedAtStr = getCellValue(row.getCell(13));
                LocalDateTime updatedAt = updatedAtStr.isEmpty() ? null :
                        LocalDateTime.parse(updatedAtStr, DATETIME_FORMATTER);

                car.setLicensePlate(licensePlate);
                car.setVinNumber(vinNumber);
                car.setColor(color);
                car.setManufacturingDate(manufacturingDate);
                car.setCurrentMileage(currentMileage);
                car.setStatus(status);
                car.setLastMaintenanceDate(lastMaintenanceDate);
                car.setNextMaintenanceMileage(nextMaintenanceMileage);
                car.setBatteryHealth(batteryHealth);
                car.setCreatedAt(createdAt);
                car.setUpdatedAt(updatedAt);

                cars.add(car);
            }
        }
        log.info("Imported {} cars from Excel file: {}", cars.size(), file);
        return cars;
    }

    /**
     * Giúp đọc giá trị của một cell dưới dạng chuỗi.
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    /**
     * Lấy giá trị số từ cell; nếu cell rỗng trả về 0.
     */
    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            String value = getCellValue(cell);
            return value.isEmpty() ? 0 : Double.parseDouble(value);
        }
    }

    /**
     * Dịch ngược trạng thái từ chuỗi của Excel về enum CarStatus.
     * Giả sử trong file export, trạng thái được in ra là:
     * - "Sẵn sàng" cho available
     * - "Đang thuê" cho rented
     * - "Bảo dưỡng" cho maintenance
     * - "Không khả dụng" cho unavailable
     */
    private CarStatus translateStatusFromString(String statusStr) {
        if (statusStr == null || statusStr.isEmpty()) return null;
        switch (statusStr.toLowerCase()) {
            case "sẵn sàng":
                return CarStatus.available;
            case "đang thuê":
                return CarStatus.rented;
            case "bảo dưỡng":
                return CarStatus.maintenance;
            case "không khả dụng":
                return CarStatus.unavailable;
            default:
                return null;
        }
    }
}