package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.FriendDto;
import com.HealthCare.HealthyLife_Backend.entity.Friend;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.repository.FreindRepository;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendService {
    private final MemberRepository memberRepository;
    private final FreindRepository freindRepository;

    // 친구/차단 목록 출력
    public List<FriendDto> getFriendList(Long memberId,boolean isFriend) {
        List<FriendDto> friends = new ArrayList<>();
        List<Friend> friendList = freindRepository.findAllByMemberId(memberId);

        for (Friend friend : friendList) {
            // isFriend 값에 따라 status와 일치하는 경우만 추가
            if (isFriend && friend.getStatus() || !isFriend && !friend.getStatus()) {
                FriendDto friendDto = FriendDto.builder()
                        .friendsId(friend.getFriendsId())
                        .status(friend.getStatus())
                        .memberId(friend.getMember().getId())
                        .build();
                friends.add(friendDto);
            }
        }
        return friends;
    }
    //친구/차단 추가 true일때 친구 / false 차단
    public boolean saveFriend(FriendDto friendDto,boolean isFriend) {
        try {
            Member member = memberRepository.findById(friendDto.getMemberId()).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재 하지 않습니다.")
            );
            Friend friend = Friend.builder()
                    .friendsId(friendDto.getFriendsId())
                    .member(member)
                    .status(isFriend)
                    .build();
            freindRepository.save(friend);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //통합삭제
    public boolean deleteFriend(Long id){
        try {
            Friend friend = freindRepository.findById(id).orElseThrow(
                    () -> new RuntimeException("해당 친구/차단이 존재하지 않습니다.")
            );
            freindRepository.delete(friend);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
