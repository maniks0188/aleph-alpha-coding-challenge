package com.shoppinglist.mgmt.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * This is the Authentication controller which provides the JWT token based on the username passed.
 *
 */
@RestController
@RequestMapping(Constants.API_BASE_PATH + "auth")
public class AuthorizationController {
	
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	private final JwtUtil jwtUtil;

    public AuthorizationController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Api for user authentication", description = "Returns an authentication token to be used in system apis")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully creates a token and returns", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "401", description = "Invalid user credentials", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "500", description = "Error while authentication", content = @Content(mediaType = "application/json"))
	      })
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
    	logger.info("Trying to login in the system...");
        String username = request.get("username");

        if ("admin".equals(username)) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token);
        }
        throw new CustomApplicationException(Constants.INVALID_USER);
    }
}
