package com.vinfast.rental_service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OrderCodeUtil {
    public static String generateOrderCode() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();

        return "ORD-" + timestamp + "-" + randomPart;
    }
}
