package com.test.fundusze.controller;

import com.test.fundusze.dto.request.InwestycjaRequest;
import com.test.fundusze.dto.response.PodzialResponse;
import com.test.fundusze.service.PodzialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fundusze")
public class FunduszController {

    private final PodzialService podzialService;

    @Autowired
    public FunduszController(PodzialService podzialService) {
        this.podzialService = podzialService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PodzialResponse> wylicz(@Valid @RequestBody InwestycjaRequest inwestycjaRequest) {
        PodzialResponse podzialResponse = podzialService.podzielInwestycje(inwestycjaRequest);
        return new ResponseEntity<>(podzialResponse, HttpStatus.OK);
    }
}
