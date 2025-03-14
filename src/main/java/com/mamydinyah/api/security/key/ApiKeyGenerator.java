package com.mamydinyah.api.security.key;

import java.util.UUID;

public class ApiKeyGenerator {
    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
