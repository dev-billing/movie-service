package com.movieservice.domain.screen.repository;

import com.movieservice.domain.screen.model.Screen;

import java.util.Optional;

public interface ScreenRepository {

    Optional<Screen> findByScreenId(long screenId);

}
