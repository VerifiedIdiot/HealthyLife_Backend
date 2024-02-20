package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.ExerciseDto;
import com.HealthCare.HealthyLife_Backend.dto.calendar.WorkoutDto;
import com.HealthCare.HealthyLife_Backend.entity.*;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import com.HealthCare.HealthyLife_Backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final BodyRepository bodyRepository;
    private final SeasonRankingRepository seasonRankingRepository;
    private final TotalRankingRepository totalRankingRepository;


    @Transactional
    public Long addAndUpdateCalendar(WorkoutDto workoutDto) {
        // 이메일을 사용하여 Member 엔티티 조회
        Member member = memberRepository.findByEmail(workoutDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + workoutDto.getEmail()));

        // 운동명을 사용하여 Exercise 엔티티 조회
        Exercise exercise = exerciseRepository.findExerciseByName(workoutDto.getWorkoutName())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found with name: " + workoutDto.getWorkoutName()));

        // Workout 엔티티 생성 및 Exercise 설정
        Workout workout = workoutDto.toWorkoutEntity();
        workout.setExercise(exercise);

        // 이메일과 날짜를 통해 Calendar 엔티티 찾기 또는 생성
        String regDate = workoutDto.getRegDate();
        String yearMonth = regDate.substring(0,6);

        Calendar calendar = calendarRepository.findByRegDateAndMemberEmail(regDate, workoutDto.getEmail())
                .orElseGet(() -> {
                    Calendar newCalendar = new Calendar();
                    newCalendar.setRegDate(regDate);
                    newCalendar.setMember(member);
                    return calendarRepository.save(newCalendar); // 새 Calendar 저장
                });

        Body latestBody = bodyRepository.findTopByMemberEmailOrderByDateDesc(workoutDto.getEmail())
                .orElse(null); // 사용자의 최신 Body 정보 조회
        if (latestBody != null) {
            calendar.setBody(latestBody); // Calendar에 최신 Body 정보 설정
        }

        TotalRanking totalRanking = totalRankingRepository.findByMemberEmail(workoutDto.getEmail())
                .orElseGet(() -> {
                    TotalRanking newTotalRanking = new TotalRanking();
                    newTotalRanking.setMember(member);
                    newTotalRanking.setPoints(0); // 초기 점수 설정
                    return newTotalRanking;
                });

        SeasonRanking seasonRanking = seasonRankingRepository.findByRegDateAndMemberEmail(yearMonth, workoutDto.getEmail())
                .orElseGet(() -> {
                    SeasonRanking newSeasonRanking = new SeasonRanking();
                    newSeasonRanking.setRegDate(yearMonth);
                    newSeasonRanking.setMember(member);
                    newSeasonRanking.setPoints(0); // 초기 점수 설정

                    totalRanking.addSeasonRanking(newSeasonRanking);

                    return newSeasonRanking;
                });

        // 운동 수행 여부에 따른 점수 할당 로직
        if (!calendar.getWorkoutAchieved()) {
            calendar.setWorkoutAchieved(true); // 운동 수행 표시
            int currentPoints = (calendar.getPoints() == null) ? 0 : calendar.getPoints();
            calendar.setPoints(currentPoints + 25); // 운동 수행으로 인한 추가 점수
            seasonRanking.setPoints(seasonRanking.getPoints() + 25);
            totalRanking.setPoints(totalRanking.getPoints() + 25);
            calendarRepository.save(calendar); // 변경된 Calendar 저장
        }

        workout.setCalendar(calendar); // Workout에 Calendar 설정
        workoutRepository.save(workout); // 새로운 Workout 저장

        return calendar.getId();
    }

    public List<ExerciseDto> getWorkoutKeyword(String keyword) {
        return exerciseRepository.findAllByName(keyword);
    }

    public List<WorkoutDto> getWorkoutByCalendarId(Long calendarId) {

        List<Workout> workouts = workoutRepository.findByCalendarId(calendarId);

        System.out.println("나오라" + workouts);
        return workouts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private WorkoutDto convertToDto(Workout workout) {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setId(workout.getId());
        workoutDto.setWorkoutName(workout.getWorkoutName());
        workoutDto.setRegDate(workout.getRegDate());

        if (workout.getExercise() != null) {
            workoutDto.setMuscle(workout.getExercise().getMuscle());
            workoutDto.setEquipment(workout.getExercise().getEquipment());
        }

        return workoutDto;
    }

    public List<WorkoutDto> findAll() {
        return workoutRepository.findAll().stream()
                .map(Workout::toWorkoutDto)
                .collect(Collectors.toList());
    }

}
