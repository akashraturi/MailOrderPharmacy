package com.mailorderpharmacy.drugservice.restclients;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mailorderpharmacy.drugservice.entity.TokenValid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "authapp", url = "http://localhost:8090/authapp")
public interface AuthFeign
{
    @GetMapping("/validate")
    ResponseEntity<TokenValid> getValidity(@RequestHeader("Authorization") final String token);
}