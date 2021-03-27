package com.move.business.service;

import com.movie.data.entity.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
