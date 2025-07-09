package net.mykull.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Mykull
// 7-8-2025
// I don't like toml files
public class JsonConfig<DATA> {
    private final File file;
    private final Class<DATA> type;
    private final Gson gson;
    private DATA data;

    /**
     *
     * @param fileName File name of config file EX: config.json
     * @param type Class to model data in the config file EX: MyConfigModel.class
     */
    public JsonConfig(String fileName, Class<DATA> type) {
        this.file = new File("config/" + fileName);
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        load(); // autoload on creation
    }

    public void load() {
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                DATA loaded = gson.fromJson(reader, type);
                if (loaded != null) {
                    data = loaded;
                } else {
                    System.err.println("[JsonConfig] JSON was null, using defaults.");
                    data = createDefault();
                    save();
                }
            } catch (Exception e) {
                System.err.println("[JsonConfig] Failed to load config: " + e.getMessage());
                data = createDefault();
                save();
            }
        } else {
            data = createDefault();
            save();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("[JsonConfig] Failed to save config: " + e.getMessage());
        }
    }

    public DATA get() {
        return data;
    }

    protected DATA createDefault() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create default config instance", e);
        }
    }
}