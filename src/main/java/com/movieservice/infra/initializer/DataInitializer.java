package com.movieservice.infra.initializer;

import com.movieservice.domain.common.model.Money;
import com.movieservice.domain.movie.enums.MovieAgeRateType;
import com.movieservice.domain.movie.model.Movie;
import com.movieservice.domain.screen.model.Screen;
import com.movieservice.domain.theater.model.Theater;
import com.movieservice.domain.theater.model.TheaterSeat;
import com.movieservice.domain.theater.model.TheaterSeatGrade;
import com.movieservice.infra.persistence.movie.entity.MovieEntity;
import com.movieservice.infra.persistence.movie.repository.MovieJpaRepository;
import com.movieservice.infra.persistence.screen.entity.ScreenEntity;
import com.movieservice.infra.persistence.screen.repository.ScreenJpaRepository;
import com.movieservice.infra.persistence.theater.entity.TheaterEntity;
import com.movieservice.infra.persistence.theater.entity.TheaterSeatEntity;
import com.movieservice.infra.persistence.theater.entity.TheaterSeatGradeEntity;
import com.movieservice.infra.persistence.theater.repository.TheaterJpaRepository;
import com.movieservice.infra.persistence.theater.repository.TheaterSeatGradeJpaRepository;
import com.movieservice.infra.persistence.theater.repository.TheaterSeatJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile({"local", "test"})
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MovieJpaRepository movieJpaRepository;
    private final TheaterJpaRepository theaterJpaRepository;
    private final TheaterSeatGradeJpaRepository theaterSeatGradeJpaRepository;
    private final ScreenJpaRepository screenJpaRepository;
    private final TheaterSeatJpaRepository theaterSeatJpaRepository;

    @Override
    public void run(String... args) {

        if (movieJpaRepository.count() > 0) {
            return ;
        }

        List<MovieEntity> movies = initializeMovies();

        // Theaters
        List<TheaterEntity> theaters = initializeTheaters();

        // Seat Grades
        List<TheaterSeatGradeEntity> seatGrades = initializeSeatGrades();

        // Screens
        initializeScreens(theaters, movies);

        // Theater Seats
        initializeTheaterSeats(theaters, seatGrades);

        log.info("Data initialization completed!");
    }

    private List<MovieEntity> initializeMovies() {
        List<MovieEntity> movies = new ArrayList<>();

        movies.add(MovieEntity.from(Movie.create("범죄도시4", LocalDate.of(2024, 4, 24), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("파묘", LocalDate.of(2024, 2, 22), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("듄: 파트2", LocalDate.of(2024, 2, 28), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("코코", LocalDate.of(2017, 12, 21), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("오펜하이머", LocalDate.of(2024, 2, 21), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("서울의 봄", LocalDate.of(2023, 11, 22), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("괴물", LocalDate.of(2006, 7, 27), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("기생충", LocalDate.of(2019, 5, 30), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("타이타닉", LocalDate.of(1998, 2, 20), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("어벤져스: 엔드게임", LocalDate.of(2019, 4, 24), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("겨울왕국2", LocalDate.of(2019, 11, 21), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("인터스텔라", LocalDate.of(2014, 11, 6), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("명량", LocalDate.of(2014, 7, 30), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("신과함께: 죄와 벌", LocalDate.of(2017, 12, 20), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("국제시장", LocalDate.of(2014, 12, 17), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("아바타", LocalDate.of(2009, 12, 17), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("베테랑", LocalDate.of(2015, 8, 5), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("택시운전사", LocalDate.of(2017, 8, 2), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("부산행", LocalDate.of(2016, 7, 20), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("암살", LocalDate.of(2015, 7, 22), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("극한직업", LocalDate.of(2019, 1, 23), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("해운대", LocalDate.of(2009, 7, 22), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("7번방의 선물", LocalDate.of(2013, 1, 23), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("왕의 남자", LocalDate.of(2005, 12, 29), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("광해, 왕이 된 남자", LocalDate.of(2012, 9, 13), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("도둑들", LocalDate.of(2012, 7, 25), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("알라딘", LocalDate.of(2019, 5, 23), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("라이언 킹", LocalDate.of(2019, 7, 17), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("토이 스토리4", LocalDate.of(2019, 6, 20), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("주토피아", LocalDate.of(2016, 2, 17), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("겨울왕국", LocalDate.of(2014, 1, 16), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("인크레더블2", LocalDate.of(2018, 7, 18), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("슈퍼배드3", LocalDate.of(2017, 7, 12), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("미니언즈", LocalDate.of(2015, 7, 22), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("다크 나이트", LocalDate.of(2008, 8, 6), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("인셉션", LocalDate.of(2010, 7, 21), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("매트릭스", LocalDate.of(1999, 5, 26), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("포레스트 검프", LocalDate.of(1994, 10, 15), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("쇼생크 탈출", LocalDate.of(1995, 2, 10), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("글래디에이터", LocalDate.of(2000, 6, 10), MovieAgeRateType.NINETEEN)));
        movies.add(MovieEntity.from(Movie.create("반지의 제왕: 왕의 귀환", LocalDate.of(2004, 2, 18), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("해리포터와 마법사의 돌", LocalDate.of(2001, 12, 14), MovieAgeRateType.ALL)));
        movies.add(MovieEntity.from(Movie.create("스파이더맨: 노 웨이 홈", LocalDate.of(2021, 12, 15), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("탑건: 매버릭", LocalDate.of(2022, 6, 22), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("조커", LocalDate.of(2019, 10, 2), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("1917", LocalDate.of(2020, 2, 19), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("테넷", LocalDate.of(2020, 8, 26), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("위플래쉬", LocalDate.of(2015, 3, 12), MovieAgeRateType.FIFTEEN)));
        movies.add(MovieEntity.from(Movie.create("라라랜드", LocalDate.of(2016, 12, 7), MovieAgeRateType.TWELVE)));
        movies.add(MovieEntity.from(Movie.create("그린 북", LocalDate.of(2019, 1, 9), MovieAgeRateType.TWELVE)));

        List<MovieEntity> savedMovies = movieJpaRepository.saveAll(movies);
        log.info("Initialized {} movies", movies.size());
        return savedMovies;
    }

    private List<TheaterEntity> initializeTheaters() {
        List<TheaterEntity> theaters = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            theaters.add(TheaterEntity.from(Theater.create(i + "관")));
        }

        List<TheaterEntity> savedTheaters = theaterJpaRepository.saveAll(theaters);
        log.info("Initialized {} theaters", theaters.size());
        return savedTheaters;
    }

    private List<TheaterSeatGradeEntity> initializeSeatGrades() {
        List<TheaterSeatGradeEntity> seatGrades = new ArrayList<>();

        seatGrades.add(TheaterSeatGradeEntity.from(TheaterSeatGrade.create("일반석", Money.create(12000))));
        seatGrades.add(TheaterSeatGradeEntity.from(TheaterSeatGrade.create("프리미엄석", Money.create(15000))));
        seatGrades.add(TheaterSeatGradeEntity.from(TheaterSeatGrade.create("VIP석", Money.create(20000))));

        List<TheaterSeatGradeEntity> savedSeatGrades = theaterSeatGradeJpaRepository.saveAllAndFlush(seatGrades);
        log.info("Initialized {} seat grades", seatGrades.size());
        return savedSeatGrades;
    }

    private void initializeScreens(List<TheaterEntity> theaters, List<MovieEntity> movies) {
        List<ScreenEntity> screens = new ArrayList<>();

        // 각 극장마다 10개 스케줄 (총 100개)
        for (int theaterIdx = 0; theaterIdx < 10; theaterIdx++) {
            Long theaterId = theaters.get(theaterIdx).getTheaterId();

            for (int i = 0; i < 5; i++) {
                int movieIdx = (theaterIdx * 5 + i) % movies.size();
                int hour = (theaterIdx % 2 == 0) ? 10 + (i * 3) : 11 + (i * 3);
                screens.add(ScreenEntity.from(Screen.create(
                        theaterId,
                        movies.get(movieIdx).getMovieId(),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(hour, 0)))));
            }

            for (int i = 0; i < 5; i++) {
                int movieIdx = (theaterIdx * 5 + i + 5) % movies.size();
                int hour = (theaterIdx % 2 == 0) ? 10 + (i * 3) : 11 + (i * 3);
                screens.add(ScreenEntity.from(Screen.create(
                        theaterId,
                        movies.get(movieIdx).getMovieId(),
                        LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.of(hour, 0)))));
            }
        }

        screenJpaRepository.saveAll(screens);
        log.info("Initialized {} screens", screens.size());
    }

    private void initializeTheaterSeats(List<TheaterEntity> theaters, List<TheaterSeatGradeEntity> seatGrades) {
        List<TheaterSeatEntity> seats = new ArrayList<>();

        TheaterSeatGrade normalSeatGrade = seatGrades.get(0).toDomain();
        TheaterSeatGrade premiumSeatGrade = seatGrades.get(1).toDomain();
        TheaterSeatGrade vipSeatGrade = seatGrades.get(2).toDomain();

        Map<Long, TheaterSeatGradeEntity> theaterSeatGradeEntityMap = seatGrades.stream()
                .collect(Collectors.toMap(TheaterSeatGradeEntity::getTheaterSeatGradeId, v -> v));

        for (TheaterEntity theater : theaters) {
            Long theaterId = theater.getTheaterId();

            char charRowName = 'A';
            for (int i = 0; i < 16; i++) {
                int columnCount = i < 4 || i > 12 ? 18 : 10;
                TheaterSeatGrade theaterSeatGrade = i < 14 ? normalSeatGrade : i == 15 ? premiumSeatGrade : vipSeatGrade;
                String rowName = String.valueOf(charRowName);
                for (int j = 0; j <columnCount; j++) {
                    TheaterSeatEntity from = TheaterSeatEntity.from(TheaterSeat.create(theaterId, theaterSeatGrade, rowName, j + 1), theaterSeatGradeEntityMap.get(theaterSeatGrade.getTheaterSeatGradeId()));
                    seats.add(from);
                }
                charRowName++;
            }
        }

        theaterSeatJpaRepository.saveAll(seats);
        log.info("Initialized {} theater seats", seats.size());
    }
}
