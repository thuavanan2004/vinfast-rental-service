package com.vinfast.rental_service.service;

import com.vinfast.rental_service.model.OtpCode;
import com.vinfast.rental_service.repository.OtpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.vinfast.rental_service.utils.OTPUtil.generateOTP;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpCodeRepository otpRepo;

    public String generateAndSaveOTP(String email) {
        String otp = generateOTP(6);
        LocalDateTime now = LocalDateTime.now();

        OtpCode otpCode = new OtpCode();
        otpCode.setEmail(email);
        otpCode.setCode(otp);
        otpCode.setCreatedAt(now);
        otpCode.setExpiresAt(now.plusMinutes(3));

        otpRepo.save(otpCode);
        return otp;
    }

    public boolean verifyOtp(String email, String code) {
        return otpRepo.findByEmailAndCode(email, code)
                .filter(otp -> !otp.isUsed())
                .filter(otp -> otp.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(otp -> {
                    otp.setUsed(true);
                    otpRepo.save(otp);
                    return true;
                })
                .orElse(false);
    }

}
