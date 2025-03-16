package com.primelife.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
public class TokenUtility {

    public static long calculateTimeDifference(LocalDateTime tokenGenerationTime){
        log.trace("Enter Method calculateTimeDifference:{}", tokenGenerationTime);

        LocalDateTime currentTime = LocalDateTime.now();
        long minutesDifference = ChronoUnit.MINUTES.between(tokenGenerationTime, currentTime);
        log.info("Exit Method calculateTimeDifference:...{}", minutesDifference);
        return minutesDifference;
    }
}
