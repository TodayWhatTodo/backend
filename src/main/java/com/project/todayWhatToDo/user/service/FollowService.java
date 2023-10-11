package com.project.todayWhatToDo.user.service;

import com.project.todayWhatToDo.user.domain.Follow;
import com.project.todayWhatToDo.user.dto.*;
import com.project.todayWhatToDo.user.exception.FollowNotFountException;
import com.project.todayWhatToDo.user.exception.UserNotFoundException;
import com.project.todayWhatToDo.user.repository.FollowRepository;
import com.project.todayWhatToDo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public void follow(FollowRequestDto request) {
        var follower = userRepository.findById(request.followerId()).orElseThrow(UserNotFoundException::new);
        var user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);

        user.addFollowing(follower);
    }

    public void followCancel(FollowCancelRequestDto request) {
        var follow = followRepository.findByFollowerIdAndFollowingId(request.followerId(), request.followingId())
                .orElseThrow(FollowNotFountException::new);

        follow.cancel();
        followRepository.delete(follow);
    }

    public Page<FollowDto> followingList(GetFollowingListRequestDto request, Pageable pageable) {
        return followRepository.findByFollowingId(request.userId(), pageable)
                .map(Follow::toDto);
    }

    public Page<FollowDto> followerList(GetFollowerListRequestDto request, Pageable pageable) {
        return followRepository.findByFollowerId(request.userId(), pageable)
                .map(Follow::toDto);
    }

    public int countFollower(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
                .getFollowerCount();
    }

    public int countFollowing(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
                .getFollowingCount();
    }
}
