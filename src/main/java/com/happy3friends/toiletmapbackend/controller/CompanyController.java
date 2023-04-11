package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Tag(name = "Company", description = "Company API")
@RestController
@RequestMapping(value = "/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Operation(summary = "Get company by account ID", description = "[Manager] Get a specific company by account ID")
    @Parameters(value = {
            @Parameter(name = "account-id", description = "A specific Account ID", in = ParameterIn.PATH, required = true, example = "1"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"Toilet Map\",\n" +
                            "    \"logo\": \"https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing\",\n" +
                            "    \"address\": \"Lô E2a-7, Đường D1\",\n" +
                            "    \"ward\": \"Phường Long Thạnh Mỹ\",\n" +
                            "    \"district\": \"Quận Thủ Đức\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"phone\": \"(028) 7300 5588\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.MANAGER})
    @GetMapping(value = "/accounts/{account-id}/company")
    public ResponseEntity<BaseResponse<CompanyResponse>> getCompanyByAccountId(@PathVariable("account-id") int accountId) {

        CompanyResponse response = companyService.getCompanyByAccountId(accountId);

        return ResponseBuilder.generateResponse(
                "Get company by account ID successfully!",
                HttpStatus.OK,
                response
        );
    }
}
