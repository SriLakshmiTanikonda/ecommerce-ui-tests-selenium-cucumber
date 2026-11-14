package com.qa.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.automation.models.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** Loads test users from testdata/users.json once and serves them by key. */
public final class JsonDataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Map<String, User> users;

    private JsonDataReader() {
    }

    public static synchronized User user(String key) {
        if (users == null) {
            users = read("testdata/users.json", new TypeReference<>() { });
        }
        User user = users.get(key);
        if (user == null) {
            throw new IllegalArgumentException("No user '" + key + "' in users.json. Known keys: " + users.keySet());
        }
        String passwordOverride = System.getenv("SAUCE_PASSWORD");
        return passwordOverride == null || passwordOverride.isBlank()
                ? user
                : new User(user.username(), passwordOverride, user.description());
    }

    private static <T> T read(String resource, TypeReference<T> type) {
        try (InputStream in = JsonDataReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing test data file: " + resource);
            }
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new IllegalStateException("Could not parse " + resource, e);
        }
    }
}
