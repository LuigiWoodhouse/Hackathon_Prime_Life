package com.primelife.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LinkUtility {
    public static String getSiteURL(HttpServletRequest request) {
        log.trace("Enter Method getSiteURL");

        try {
            String requestURL = request.getRequestURL().toString();
            log.info("Request URL: {}", requestURL);

            String servletPath = request.getServletPath();
            String siteURL = servletPath != null ? requestURL.replace(servletPath, "") : requestURL;
            log.info("Site URL: {}", siteURL);

            return siteURL;
        } catch (Exception e) {
            log.error("Return Method getSiteURL: An error occurred while generating url : " + e.getMessage());
            throw new RuntimeException("Failed to generate url", e);
        }
    }

}
