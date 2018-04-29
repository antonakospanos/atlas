package org.antonakospanos.iot.atlas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String hashedPassord) {
        return passwordEncoder.matches(rawPassword, hashedPassord);
    }
}