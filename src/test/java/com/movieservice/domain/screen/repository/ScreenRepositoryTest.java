package com.movieservice.domain.screen.repository;

import com.movieservice.IntegrationTestSupport;
import com.movieservice.domain.screen.model.Screen;
import com.movieservice.infra.persistence.screen.entity.ScreenEntity;
import com.movieservice.infra.persistence.screen.repository.ScreenJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class ScreenRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ScreenJpaRepository screenJpaRepository;

    @Test
    @DisplayName("ScreenId 로 Screen 조회한다.")
    void findScreenByScreenId(){
        // given
        Screen screen = Screen.create(1L, 1L, LocalDateTime.now());
        ScreenEntity screenEntity = ScreenEntity.from(screen);
        ScreenEntity savedScreen = screenJpaRepository.save(screenEntity);

        Long screenId = savedScreen.getScreenId();
        // when
        Optional<Screen> findScreen = screenRepository.findByScreenId(screenId);

        // then
        assertThat(findScreen.isPresent()).isTrue();
        assertThat(findScreen.get().getScreenId()).isEqualTo(screenId);
    }

    @Test
    @DisplayName("존재하지 않은 ScreenId 로 조회하면 빈 Optional 반환한다.")
    void findScreenByWrongScreenIdReturnBlankOptional(){
        // given
        long screenId = -1L;

        // when
        Optional<Screen> findScreen = screenRepository.findByScreenId(screenId);

        // then
        assertThat(findScreen.isPresent()).isFalse();
    }
}