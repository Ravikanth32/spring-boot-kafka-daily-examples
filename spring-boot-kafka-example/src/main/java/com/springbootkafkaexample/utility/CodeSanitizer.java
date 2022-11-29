package com.springbootkafkaexample.utility;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Component;

@Component
public class CodeSanitizer {
    private static String cleanUp(String value) {
        String result;
        String finalResult;
        if (value != null && !((String) value).isEmpty()) {
            result = Jsoup.clean(value, Whitelist.basic());
            finalResult = Parser.unescapeEntities(result, true);
        } else {
            value = "";
            finalResult = Jsoup.clean(value, Whitelist.basic());
            finalResult = null;
        }
        return finalResult;
    }

    public static Long cleanUpLong(Long param) {
        String result;
        Long finalResult = new Long(0L);
        if (param != null) {
            result = String.valueOf(param);
            result = cleanUp(result);
            finalResult = Long.valueOf(result);
        }
        return finalResult;
    }

    public static String cleanUpString(String value) {
        return cleanUp(value);
    }
}

