package com.movieservice.infra.persistence.screen.repository;

import com.movieservice.domain.screen.model.Screen;
import com.movieservice.domain.screen.repository.ScreenRepository;
import com.movieservice.infra.persistence.screen.entity.ScreenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScreenRepositoryImpl implements ScreenRepository {

    private final ScreenJpaRepository screenJpaRepository;

    @Override
    public Optional<Screen> findByScreenId(long screenId) {
        return screenJpaRepository.findById(screenId)
                .map(ScreenEntity::toDomain);
    }
}
