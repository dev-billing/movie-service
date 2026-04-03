package com.movieservice.infra.persistence.screen.repository;

import com.movieservice.infra.persistence.screen.entity.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenJpaRepository extends JpaRepository<ScreenEntity, Long> {
}
