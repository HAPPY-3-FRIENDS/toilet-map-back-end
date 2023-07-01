package com.happy3friends.toiletmapbackend.config;

import com.happy3friends.toiletmapbackend.constant.VNPayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

public class VNPayConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(VNPayConfig.class);
    public static String vnp_Version = VNPayConstant.VNP_VERSION;
    public static String vnp_Command = VNPayConstant.VNP_COMMAND;
    public static String vnp_TmnCode = VNPayConstant.VNP_TERMINAL_ID;
    public static String vnp_CurrCode = VNPayConstant.VNP_CURRENCY_CODE;
    public static String vnp_BankCode = VNPayConstant.VNP_DEFAULT_BANK_CODE;
    public static String vnp_OrderType = VNPayConstant.VNP_ORDER_TYPE;
    public static String vnp_Locale = VNPayConstant.VNP_LOCALE;
    public static String vnp_PayUrl = VNPayConstant.VNP_PAY_URL;
    public static String vnp_HashSecret = VNPayConstant.VNP_SECRET_KEY;

    public static String getVnpReturnUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        return baseUrl.concat("/api/payments/VNPay-response");
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            LOGGER.error("VNPay Config - hmacSHA512: ", ex);
        }
        return null;
    }

    public static String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getLocalAddr();
            }
        } catch (Exception ex) {
            ipAddress = "Invalid IP:" + ex.getMessage();
            LOGGER.error("VNPay Config - getIpAddress: ", ex);
        }
        return ipAddress;
    }
}
