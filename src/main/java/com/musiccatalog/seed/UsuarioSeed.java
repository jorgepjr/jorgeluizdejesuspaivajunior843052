package com.musiccatalog.seed;

import com.musiccatalog.model.account.User;
import com.musiccatalog.model.account.UserRole;
import com.musiccatalog.repository.account.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioSeed implements ApplicationRunner {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioSeed(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (repository.count() > 0) return;

        User admin = new User("admin", encoder.encode("admin"), UserRole.ADMIN);
        User user = new User("user", encoder.encode("user"), UserRole.USER);

        repository.saveAll(List.of(admin, user));
    }
}
