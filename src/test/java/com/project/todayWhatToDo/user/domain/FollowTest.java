package com.project.todayWhatToDo.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.registerCustomDateFormat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    @DisplayName("팔로우 정보를 가진 dto 객체를 반환한다")
    @Test
    public void toDto() {
        //given
        var follower = mock(User.class);
        var following = mock(User.class);
        var follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        given(follower.getId()).willReturn(1L);
        given(follower.getNickname()).willReturn("user1");
        given(following.getId()).willReturn(2L);
        given(following.getNickname()).willReturn("user2");
        //when
        var dto = follow.toDto();
        //then
        assertThat(dto).extracting("followerId", "followingId", "followerNickname", "followingNickname")
                .containsExactly(1L, 2L, "user1", "user2");
    }
}
