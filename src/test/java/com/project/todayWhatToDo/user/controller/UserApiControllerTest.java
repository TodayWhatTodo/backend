package com.project.todayWhatToDo.user.controller;

import com.project.todayWhatToDo.RestDocsTest;
import com.project.todayWhatToDo.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("유저 API")
class UserApiControllerTest extends RestDocsTest {
    @MockBean
    UserService userService;

}