package util;

import data.dbDTO.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Christian on 04-08-2017.
 */
public class CSVParser {

    public static List<User> getUsersFromCsv(String csv){
        List<User> userList = new ArrayList<>();
        if (csv.contains(";")){ //CN-dump
            CSVFormat format = CSVFormat.EXCEL.withDelimiter(';').withHeader();
            try {
                org.apache.commons.csv.CSVParser parse = org.apache.commons.csv.CSVParser.parse(csv, format);
                userList = parseUsers(parse);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else { // Comma separated list
            String[] split = csv.split(",");
            for (int i = 0; i < split.length; i++) {
                userList.add(new User(split[i].trim()));
            }
        }
        return userList;
    }

    private static List<User> parseUsers(org.apache.commons.csv.CSVParser parse) {
        List<User> userList = new ArrayList<>();
        Map<String, Integer> headerMap = parse.getHeaderMap();
        List<CSVRecord> records = null;
        try {
            records = parse.getRecords();
        } catch (IOException e) {
            return null;
        }
        for(CSVRecord record : records){
            User newUser = new User();
            newUser.setUserName(record.get("Brugernavn"));
            newUser.setFirstName(record.get("Fornavn"));
            newUser.setLastName(record.get("Efternavn"));
            newUser.setEmail(record.get("Email"));
            System.out.println(record.get("Email"));
            userList.add(newUser);
        }
        return userList;


    }
}
