package com.planet.develop.Controller;

import com.planet.develop.DTO.MissionCompleteDto;
import com.planet.develop.Entity.Mission;
import com.planet.develop.Entity.MissionComplete;
import com.planet.develop.Entity.User;
import com.planet.develop.Login.JWT.JwtProperties;
import com.planet.develop.Login.Model.KakaoUser;
import com.planet.develop.Login.Repository.KakaoUserRepository;
import com.planet.develop.Repository.MissionRepository;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Service.MissionCompleteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class MissionController {
    private final UserRepository userRepository;
    private final MissionCompleteService missionCompleteService;
    private final MissionRepository missionRepository;

    /** 에코미션 데이터 저장 */
    @PostMapping("/mission/{emoji}/{name}")
    public void main(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("emoji") String emoji, @PathVariable("name")String name){
        User user = userRepository.findById(userId).get();
        MissionComplete mission = MissionComplete.builder()
                .emoji(emoji)
                .name(name)
                .user(user)
                .date(LocalDate.now())
                .build();
        missionCompleteService.save(mission);
    }

    /** 에코미션 페이지 조회 */
    @GetMapping("/mission/{year}/{month}")
    public Result mission(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month){
        User user = userRepository.findById(userId).get();
        Mission mission = missionRepository.findMission(LocalDate.now());

        System.out.println("데일리 미션: " + mission.getName());
        System.out.println("데일리 미션 이모지: " + mission.getEmoji());

        MissionCompleteDto todayMission = new MissionCompleteDto(mission.getName(),mission.getEmoji());

        List<MissionComplete> missions = missionCompleteService.findMissions(user,year,month);
        List<MissionCompleteDto> missionCompleteDtos = missions.stream()
                .map(o->new MissionCompleteDto(o.getName(),o.getEmoji()))
                .collect(Collectors.toList());

        if (missions == null) {
            System.out.println("아직 달성하지 않았어요");
        } else {
            System.out.println("달성했어요!");
        }


        return new Result(todayMission,missionCompleteDtos);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T todayMission;
        private T missions;
    }
}