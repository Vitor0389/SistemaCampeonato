package br.ifsp.demo.security.builder;

import br.ifsp.demo.security.user.Role;
import br.ifsp.demo.security.user.User;
import com.github.javafaker.Faker;

import java.util.UUID;

public class EntityBuilder {
    private static final Faker faker = Faker.instance();

    public static User createRandomUser(String password) {
        return User.builder().id(UUID.randomUUID())
                .name(faker.name().firstName())
                .lastname(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(password)
                .role(Role.USER)
                .build();
    }
}
