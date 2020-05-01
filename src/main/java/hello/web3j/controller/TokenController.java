package hello.web3j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import io.swagger.annotations.ApiOperation;


import hello.web3j.service.Web3jSampleService;

@RestController
public class TokenController {

	@Autowired
	private Web3jSampleService web3jSvc;

	@RequestMapping(method = RequestMethod.GET, value = "/symbol")
	public String symbol() {
		return web3jSvc.getTokenName();
	}
	
   @RequestMapping(method = RequestMethod.GET, value = "/getBalance/{address}")
   @ApiOperation(value = "Get token balance from address")
	public BigInteger getBalance(@PathVariable("address") String address) {
		return web3jSvc.getAddressBalance(address);
	}
}
