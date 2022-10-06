package com.obolonyk.templator.reader;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;

public class ResourceReader {

    @SneakyThrows
    public static String getStringFromFile(String pathToFile) {
        File file = new File(pathToFile);
        StringBuilder stringBuilder = new StringBuilder();
        String lineFromFile;
        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file))) {
            while ((lineFromFile = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineFromFile);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString().strip();
    }
}
