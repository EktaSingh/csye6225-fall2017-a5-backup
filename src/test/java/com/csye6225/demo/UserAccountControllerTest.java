package com.csye6225.demo;

import com.csye6225.demo.controllers.UserController;
import com.csye6225.demo.datalayer.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
<Ekta Singh>, <001258567>, <singh.ek@husky.neu.edu>
<Karan Bhavsar>, <001225621>, <bhavsar.ka@husky.neu.edu>
<Bhavesh Sachdev>, <001280940>, <sachdev.b@husky.neu.edu>
<Nikita Dulani>, <001280944>, <dulani.n@husky.neu.edu>
*/

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class UserAccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }

    @Test
    public void testValidateUser()
    {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        when(mockRequest.getHeader("Authorization")).thenReturn("Basic YUBhLmNvbTph");
        assertThat(userController.validateUser(mockRequest).contains("you are logged in."));

        when(mockRequest.getHeader("Authorization")).thenReturn("Basic YUBhLmNvbTpo");
        assertThat(userController.validateUser(mockRequest).contains("you are not authorized!!!"));

    }


}
