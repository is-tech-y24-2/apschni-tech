package ru.ifmo.banks.clients;


import lombok.*;

import java.util.UUID;

@Data
@Builder
public class Client {
    private final UUID id = UUID.randomUUID();
    private String name;
    private String surname;
    private String address;
    private String passport;

    public Boolean isVerified() {
        if (address == null || passport == null) return false;
        return !address.isEmpty() && !passport.isEmpty();
    }

    public void getUpdate(String message) {
        System.out.println(message);
    }
}
