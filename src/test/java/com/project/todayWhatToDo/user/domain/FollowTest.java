package com.project.todayWhatToDo.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class FollowTest {

    @DisplayName("팔로우 취소시 팔로잉 수와 팔로워 수가 1씩 줄어든다.")
    @Test
    public void followCancel() {
        //given
        var follower = User.builder().build();
        var following = User.builder().build();

        var follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        //when
        follow.cancel();
        //then
        assertThat(follower.getFollowerCount()).isEqualTo(-1);
        assertThat(following.getFollowingCount()).isEqualTo(-1);
    }
}
