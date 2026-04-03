package com.movieservice.domain.theater.repository;

import com.movieservice.IntegrationTestSupport;
import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.theater.model.TheaterSeat;
import com.movieservice.domain.theater.model.TheaterSeatGrade;
import com.movieservice.infra.persistence.theater.entity.TheaterSeatEntity;
import com.movieservice.infra.persistence.theater.entity.TheaterSeatGradeEntity;
import com.movieservice.infra.persistence.theater.repository.TheaterSeatGradeJpaRepository;
import com.movieservice.infra.persistence.theater.repository.TheaterSeatJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class TheaterRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private TheaterSeatJpaRepository theaterSeatJpaRepository;

    @Autowired
    private TheaterSeatGradeJpaRepository theaterSeatGradeJpaRepository;

    @Test
    @DisplayName("TheaterSeatId로 영화관 좌석 정보를 조회할 수 있다.")
    void findAllTheaterSeatByTheaterSeatIds(){
        // given
        TheaterSeatGrade theaterSeatGrade = TheaterSeatGrade.create("일반석", Money.create(10000));
        TheaterSeatGradeEntity theaterSeatGradeEntity = TheaterSeatGradeEntity.from(theaterSeatGrade);
        theaterSeatGradeJpaRepository.save(theaterSeatGradeEntity);
        TheaterSeat theaterSeat1 = TheaterSeat.create(1L, theaterSeatGrade, "A", 1);
        TheaterSeat theaterSeat2 = TheaterSeat.create(1L, theaterSeatGrade, "A", 2);
        TheaterSeatEntity theaterSeatEntity1 = TheaterSeatEntity.from(theaterSeat1, theaterSeatGradeEntity);
        TheaterSeatEntity theaterSeatEntity2 = TheaterSeatEntity.from(theaterSeat2, theaterSeatGradeEntity);
        List<TheaterSeatEntity> theaterSeatEntities = List.of(theaterSeatEntity1, theaterSeatEntity2);
        List<TheaterSeatEntity> saveTheaterSeatEntities = theaterSeatJpaRepository.saveAll(theaterSeatEntities);

        List<Long> saveTheaterSeatIds = saveTheaterSeatEntities.stream()
                .map(TheaterSeatEntity::getTheaterSeatId)
                .toList();

        // when
        List<TheaterSeat> allTheaterSeatByTheaterSeatIds = theaterRepository.findAllTheaterSeatByTheaterSeatIds(saveTheaterSeatIds);

        // then
        assertThat(allTheaterSeatByTheaterSeatIds).hasSize(2)
                .extracting(TheaterSeat::getTheaterSeatId)
                .containsExactlyInAnyOrder(saveTheaterSeatIds.get(0), saveTheaterSeatIds.get(1));
    }

    @Test
    @DisplayName("존재 하지 않은 TheaterSeatId로 조회할 경우 빈 리스트가 반환한다.")
    void findNotExistTheaterSeatIdReturnEmptyList(){
        // given
        List<Long> theaterSeatIds = List.of(-1L, -2L);

        // when
        List<TheaterSeat> allTheaterSeatByTheaterSeatIds = theaterRepository.findAllTheaterSeatByTheaterSeatIds(theaterSeatIds);

        // then
        assertThat(allTheaterSeatByTheaterSeatIds).isEmpty();
    }
}