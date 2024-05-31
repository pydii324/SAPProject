package com.demo.service.logic;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ImageDecoder {
    // byteArrayToHexString
    public String decodeImage(byte[] bytes) {
        if (bytes != null)
            return Base64.getEncoder().encodeToString(bytes);
        return "";
    }
}
