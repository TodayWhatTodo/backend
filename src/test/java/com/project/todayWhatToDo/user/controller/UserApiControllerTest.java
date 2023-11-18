package com.project.todayWhatToDo.user.controller;

import com.project.todayWhatToDo.RestDocsTest;
import com.project.todayWhatToDo.common.WithCommonUser;
import com.project.todayWhatToDo.user.dto.response.ProfileResponse;
import com.project.todayWhatToDo.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 API")
class UserApiControllerTest extends RestDocsTest {

    @MockBean
    UserService userService;

    @WithCommonUser(userId = 1L, nickname = "testuser")
    @DisplayName("유저 조회시 성공시 프로필 데이터를 반환한다.")
    @Test
    void getUserProfile() throws Exception {
        // given 유저 조회시 다음 데이터를 반환한다
        given(userService.getProfile(any()))
                .willReturn(new ProfileResponse(
                        "/profile.png",
                        100,
                        34,
                        "testuser",
                        "hello nice to meet you",
                        "대리",
                        "(주) 중소기업",
                        1L
                ));

        // when // then
        mockMvc.perform(get("/api/v1/user")
                        .header("Authorization", "Bearer ...")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("성공"),
                        jsonPath("$.data.profileImagePath").value("/profile.png"),
                        jsonPath("$.data.followerCount").value(100),
                        jsonPath("$.data.followingCount").value(34),
                        jsonPath("$.data.nickname").value("testuser"),
                        jsonPath("$.data.introduction").value("hello nice to meet you"),
                        jsonPath("$.data.position").value("대리"),
                        jsonPath("$.data.company").value("(주) 중소기업"),
                        jsonPath("$.data.userId").value(1L)
                )
                .andDo(
                        document("유저 프로필 조회 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer jwt 엑세스 토큰"),
                                        headerWithName("Accept-Language").description("언어 지정 : ko or en").optional()
                                ),
                                responseFields(
                                        fieldWithPath("status").description("응답 상태 메시지"),
                                        fieldWithPath("data.profileImagePath").description("프로필 이미지 조회 경로"),
                                        fieldWithPath("data.followerCount").description("팔로워 수"),
                                        fieldWithPath("data.followingCount").description("팔로잉한 사람 수"),
                                        fieldWithPath("data.nickname").description("닉네임"),
                                        fieldWithPath("data.introduction").description("자기소개"),
                                        fieldWithPath("data.position").description("제직 회사 직책"),
                                        fieldWithPath("data.company").description("제직 회사명"),
                                        fieldWithPath("data.userId").description("유저 식별자")
                                )
                        )
                );
    }

}