package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeJSON(json, "data.json");
        list = parseXML("data.xml");
        json = listToJson(list);
        writeJSON(json, "data2.json");
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


    private static List<Employee> parseXML(String fileName) {
        List<Employee> result = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            NodeList empList = root.getChildNodes();
            Long id = null;
            String firstName = null;
            String lastName = null;
            String country = null;
            Byte age = null;
            for (int i = 0; i < empList.getLength(); i++) {
                if (empList.item(i).getNodeName() == "employee") {
                    NodeList paramList = empList.item(i).getChildNodes();
                    for (int j = 0; j < paramList.getLength(); j++) {
                        switch (paramList.item(j).getNodeName()) {
                            case ("id"):
                                id = Long.parseLong(paramList.item(j).getTextContent());
                                break;
                            case ("firstName"):
                                firstName = paramList.item(j).getTextContent();
                                break;
                            case ("lastName"):
                                lastName = paramList.item(j).getTextContent();
                                break;
                            case ("country"):
                                country = paramList.item(j).getTextContent();
                                break;
                            case ("age"):
                                age = Byte.parseByte(paramList.item(j).getTextContent());
                                break;
                            default:
                                break;
                        }
                    }
//                    System.out.println(id + firstName + lastName + country + age);
                    result.add(new Employee(id, firstName, lastName, country, age));
                }
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
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
