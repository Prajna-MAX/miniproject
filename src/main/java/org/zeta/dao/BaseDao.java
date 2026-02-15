package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeta.model.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao<T> {

    protected final ObjectMapper mapper = new ObjectMapper();
    protected final File file;
    protected List<T> dataList = new ArrayList<>();

    public BaseDao(String fileName, TypeReference<List<T>> typeRef) {
        this.file = new File(fileName);
        mapper.findAndRegisterModules(); // for LocalDate support
        loadFromFile(typeRef);
    }

    private void loadFromFile(TypeReference<List<T>> typeRef) {
        try {
            if (file.exists() && file.length() > 0) {
                dataList = mapper.readValue(file, typeRef);
            } else {
                dataList = new ArrayList<>();
                saveToFile();
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
            dataList = new ArrayList<>();
        }
    }

    protected void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, dataList);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public List<T> getAll() {
        return new ArrayList<>(dataList);
    }

    public void add(T obj) {
        dataList.add(obj);
        saveToFile();
    }

    protected void remove(T obj) {
        dataList.remove(obj);
        saveToFile();
    }
}
