package com.HealthCare.HealthyLife_Backend.service.calendar;

import com.HealthCare.HealthyLife_Backend.dto.calendar.WorkoutDto;
import com.HealthCare.HealthyLife_Backend.entity.Exercise;
import com.HealthCare.HealthyLife_Backend.entity.Food;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Calendar;
import com.HealthCare.HealthyLife_Backend.entity.calendar.Workout;
import com.HealthCare.HealthyLife_Backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class WorkoutService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    public WorkoutService(CalendarRepository calendarRepository, MemberRepository memberRepository, ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.calendarRepository = calendarRepository;

        this.memberRepository = memberRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }


    @Transactional
    public void addAndUpdateCalendar(WorkoutDto workoutDto) {
        // 이메일을 사용하여 Member 엔티티 조회
        Member member = memberRepository.findByEmail(workoutDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with email: " + workoutDto.getEmail()));
        // WorkoutDto로부터 Workout 엔티티 변환
        Workout workout = workoutDto.toWorkoutEntity();

        // 운동명을 사용하여 Exerice 엔티티 조회 ㅎㅎ
        Exercise exercise = exerciseRepository.findExerciseByName(workoutDto.getWorkoutName())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found with name: " + workout.getWorkoutName()));
        workout.setExercise(exercise);

        // 이메일을 통해 조회된 Member와 연관된 Calendar 엔티티 찾기 또는 생성
        String regDate = workout.getRegDate();
        Calendar calendar = calendarRepository.findByRegDateAndMemberEmail(regDate, workoutDto.getEmail())
                .orElseGet(() -> {
                    Calendar newCalendar = new Calendar();
                    newCalendar.setRegDate(regDate);
                    newCalendar.setMember(member); // Calendar에 Member 설정
                    return calendarRepository.save(newCalendar); // 새 Calendar 저장
                });

        workout.setCalendar(calendar); // Workout에 Calendar 설정

        // Workout 저장
        workoutRepository.save(workout);
    }
}
