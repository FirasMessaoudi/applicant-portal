/*
 * Copyright (c) 2019 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.services.otp;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * OTP number generator
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Service
class OtpGenerator {

    private static final long T_ZERO = 0L;
    private static final long T_ONE = 30000L;
    private static final String ALG = "HmacSHA1";

    @Value("${otp.pin.length}")
    private int pinLength;

    /**
     * Generates an OTP for the provided key
     *
     * @param key the key to generate the otp
     * @return the generated otp
     * @throws NoSuchAlgorithmException in case of unknown algorithm
     * @throws InvalidKeyException in case of invalid key
     */
    public String generateOtp(String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Assert.hasText(key, "Key is mandatory");

        /*
         * STEP 1: Calculate C as the number of times TI has elapsed after T0.
         */
        long now = System.currentTimeMillis();
        long c = (now - T_ZERO) / T_ONE;

        /*
         * convert C to a byte array
         */
        byte[] cBytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            cBytes[i] = (byte) c;
            c >>>= 8;
        }
        /*
         * STEP 2: Compute the HMAC hash H with C as the message and K as the key (the HMAC algorithm is defined in the
         * previous section, but also most cryptographical libraries support it). K should be passed as it is, C should
         * be passed as a raw 64-bit unsigned integer.
         */
        byte[] k = getKeyBytes(key);

        SecretKeySpec signer = new SecretKeySpec(k, ALG);
        Mac mac = Mac.getInstance(ALG);
        mac.init(signer);

        byte[] h = mac.doFinal(cBytes);

        /*
         * STEP 3: Take the least 4 significant bits of H and use it as an offset, O.
         */
        int o = h[h.length - 1] & 0x0F;

        /*
         * STEP 4: Take 4 bytes from H starting at O bytes MSB, discard the most significant bit and store the rest as
         * an (unsigned) 32-bit integer, I.
         */
        long i = 0;
        for (int idx = 0; idx < 4; ++idx) {
            i <<= 8;
            i |= (h[o + idx] & 0xFF);
        }

        i &= 0x7FFFFFFF;

        /*
         * STEP 5: The token is the lowest N digits of I in base 10. If the result has fewer digits than N, pad it with
         * zeroes from the left.
         */
        i %= Math.pow(10, pinLength);
        return String.format("%0" + pinLength + "d", i);
    }

    /**
     * creates a byte array for the given key
     *
     * @param key the source key
     * @return a byte array
     */
    private byte[] getKeyBytes(String key) {
        StringBuilder trimmed = new StringBuilder();
        for (int i = 0; i < key.length(); ++i) {
            char c = key.charAt(i);
            if (!Character.isWhitespace(c)) {
                trimmed.append(c);
            }
        }

        while ((trimmed.length() % 8) > 0) {
            trimmed.append('=');
        }

        String base32 = trimmed.toString().toUpperCase(Locale.ENGLISH);
        Base32 decoder = new Base32();

        return decoder.decode(base32);
    }

}
