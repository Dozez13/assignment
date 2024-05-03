package com.example.assignment.config;

import com.example.assignment.core.config.JacksonJsonNullableModuleConfiguration;
import com.example.assignment.core.service.UserService;
import com.example.assignment.datagenerator.dto.UserGetDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPatchDtoGenerator;
import com.example.assignment.datagenerator.dto.UserPostPutDtoGenerator;
import com.example.assignment.web.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import({GeneralTestConfig.class, JacksonJsonNullableModuleConfiguration.class})
@TestPropertySource(properties = {"adult.edge.point = 18"})
@ActiveProfiles("test")
public abstract class AbstractUnitWebTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected UserService userService;

}
