package com.honglog.api.service;


import com.honglog.api.domain.Session;
import com.honglog.api.domain.User;
import com.honglog.api.exception.InvalidSigninInformation;
import com.honglog.api.repository.UserRepository;
import com.honglog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login request) {

        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();

        return user.getId();
    }
}
