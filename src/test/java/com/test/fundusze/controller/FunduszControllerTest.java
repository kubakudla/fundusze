package com.test.fundusze.controller;

import com.test.fundusze.dto.request.InwestycjaRequest;
import com.test.fundusze.dto.response.PodzialResponse;
import com.test.fundusze.service.PodzialService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FunduszControllerTest {

    @Mock
    private PodzialService podzialService;

    @InjectMocks
    private FunduszController funduszService;

    @Mock
    private InwestycjaRequest inwestycjaRequest;


    @Test
    public void test_sucess(){
        //when
        ResponseEntity<PodzialResponse> podzialResponse = funduszService.wylicz(inwestycjaRequest);

        //then
        verify(podzialService, times(1)).podzielInwestycje(inwestycjaRequest);
        assertEquals(HttpStatus.OK, podzialResponse.getStatusCode());
    }

}
