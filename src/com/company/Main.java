package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeJSON(json, "data.json");
        System.out.println(json);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> result = null;
        try {
            FileReader filereader = new FileReader(fileName);
            CSVReader csvReader = new CSVReader(filereader);
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            result = csvToBean.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }


    private static void writeJSON(String string, String fileName) {
        try {
            FileWriter file = new FileWriter(fileName);
            file.write(string);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
