package business.impl;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Christian on 31-05-2017.
 */
public class GoogleSheetParser {
    private static final int GOOGLE_EPOCH_OFFSET = 25569; //Days between 30/12 1899 and 1/1/1970

    protected static Date convertGoogleDate(Double numberValue) {
        numberValue-= GOOGLE_EPOCH_OFFSET; //Fix google offset 30/12 1899
        long millisSince1970 = (long) (numberValue * 24*60*60*1000); //Convert from days to millis
        long rest = millisSince1970 %1000; // Round off millis to avoid rounding errors (13:00 -> 12:59)
        millisSince1970 += (rest>500 ?rest : -rest);
        return new Date(millisSince1970);
    }

    protected static String parseLinkForSheetId(String link) throws IdNotFoundException {
        if (link != null) {
            String[] urlParts = link.split("/");
            if ((urlParts.length >= 4 && "spreadsheets".equals(urlParts[3])) || (link.toLowerCase().contains("google") && link.toLowerCase().contains("spreadsheets"))) {
                Pattern sheetsIdPattern = Pattern.compile("[-\\w]{25,}");
                Matcher matcher = sheetsIdPattern.matcher(link);
                if (matcher.find()){
                    return matcher.group();
                } else {
                    throw new IdNotFoundException();
                }
            } else {
                throw new IdNotFoundException();
            }
        } else {
            throw new IdNotFoundException();
        }
    }

    protected static class IdNotFoundException extends Exception {
    }
}
