package com.algaworks.algashop.product.catalog.application.utility;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;

public class ImageMediaTypeExtractor {

    public static MediaType fromFileName(String fileName) {
        String extention = FilenameUtils.getExtension(fileName);
        if (extention.equalsIgnoreCase("jpg")){
            extention = "jpg";
        }

        return MediaType.valueOf("image/" + extention.toLowerCase());
    }
}
