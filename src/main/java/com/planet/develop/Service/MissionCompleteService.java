package com.planet.develop.Service;

import com.planet.develop.Entity.MissionComplete;
import com.planet.develop.Login.Model.KakaoUser;
import com.planet.develop.Repository.MissionCompleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionCompleteService {
    private final MissionCompleteRepository missionCompleteRepository;

    /** 미션 저장 **/
    public Long save(MissionComplete mission) {
        missionCompleteRepository.save(mission);
        return mission.getId();
    }

    public List<MissionComplete> findMissions(KakaoUser kakaoUser, int year, int month){
        List<MissionComplete> missions = missionCompleteRepository.findMissions(kakaoUser,year,month);
        return missions;
    }
}