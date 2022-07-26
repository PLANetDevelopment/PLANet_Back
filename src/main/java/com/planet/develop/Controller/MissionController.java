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

//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://main.d2f9fwhj50mv28.amplifyapp.com")
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@RestController
public class MissionController {
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final MissionCompleteService missionCompleteService;
    private final MissionRepository missionRepository;

    /** 에코미션 데이터 저장*/
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
    
    /** 에코미션 데이터 저장 - 테스트용 */
//    @PostMapping("/{id}/mission/{emoji}/{name}")
//    public void main(@PathVariable("id") String id, @PathVariable("emoji") String emoji, @PathVariable("name")String name){
//        User user = userRepository.findById(id).get();
//        MissionComplete mission = MissionComplete.builder()
//                .emoji(emoji)
//                .name(name)
//                .user(user)
//                .date(LocalDate.now())
//                .build();
//        missionCompleteService.save(mission);
//    }

    /** 에코미션 페이지 조회*/
    @GetMapping("/mission/{year}/{month}")
    public Result mission(@RequestHeader(JwtProperties.USER_ID) String userId, @PathVariable("year") int year, @PathVariable("month") int month){
        KakaoUser kakaoUser = kakaoUserRepository.findByKakaoEmail(userId);
        Mission mission = missionRepository.findMission(LocalDate.now());
        MissionCompleteDto todayMission = new MissionCompleteDto(mission.getName(),mission.getEmoji());

        List<MissionComplete> missions = missionCompleteService.findMissions(kakaoUser,year,month);
        List<MissionCompleteDto> missionCompleteDtos = missions.stream()
                .map(o->new MissionCompleteDto(o.getName(),o.getEmoji()))
                .collect(Collectors.toList());
        return new Result(todayMission,missionCompleteDtos);

    }
    
    /** 에코미션 페이지 조회 - 테스트용 */
//    @GetMapping("/{id}/mission/{year}/{month}")
//    public Result mission(@PathVariable("id") String id, @PathVariable("year") int year, @PathVariable("month") int month){
//        User user = userRepository.findById(id).get();
//        Mission mission = missionRepository.findMission(LocalDate.now());
//        MissionCompleteDto todayMission = new MissionCompleteDto(mission.getName(),mission.getEmoji());
//
//        List<MissionComplete> missions = missionCompleteService.findMissions(user,year,month);
//        List<MissionCompleteDto> missionCompleteDtos = missions.stream()
//                .map(o->new MissionCompleteDto(o.getName(),o.getEmoji()))
//                .collect(Collectors.toList());
//        return new Result(todayMission,missionCompleteDtos);
//
//    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T todayMission;
        private T missions;
    }
}