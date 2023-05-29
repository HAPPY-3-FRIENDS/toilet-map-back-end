package com.happy3friends.toiletmapbackend.controller;

import com.happy3friends.toiletmapbackend.base.models.BasePaginationRequest;
import com.happy3friends.toiletmapbackend.base.models.BaseResponse;
import com.happy3friends.toiletmapbackend.config.OpenApiConfig;
import com.happy3friends.toiletmapbackend.constant.RoleConstant;
import com.happy3friends.toiletmapbackend.handler.ResponseBuilder;
import com.happy3friends.toiletmapbackend.request.CompanyCreateRequest;
import com.happy3friends.toiletmapbackend.response.CompanyHasStatusResponse;
import com.happy3friends.toiletmapbackend.response.CompanyResponse;
import com.happy3friends.toiletmapbackend.response.UpdateCompanyResponse;
import com.happy3friends.toiletmapbackend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    @Operation(summary = "Create a company", description = "[Admin] Create a company and its information")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Company Create Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"name\": \"Toilet Map\",\n" +
                            "    \"logo\": \"https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing\",\n" +
                            "    \"address\": \"Lô E2a-7, Đường D1\",\n" +
                            "    \"ward\": \"Phường Long Thạnh Mỹ\",\n" +
                            "    \"district\": \"Quận Thủ Đức\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"phone\": \"(028) 7300 5588\",\n" +
                            "    \"username\": \"manager-1\",\n" +
                            "    \"password\": \"$2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG\"\n" +
                            "  }")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"name\": \"Toilet Map\",\n" +
                            "    \"logo\": \"https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing\",\n" +
                            "    \"address\": \"Lô E2a-7, Đường D1\",\n" +
                            "    \"ward\": \"Phường Long Thạnh Mỹ\",\n" +
                            "    \"district\": \"Quận Thủ Đức\",\n" +
                            "    \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "    \"phone\": \"(028) 7300 5588\",\n" +
                            "    \"username\": \"manager-1\",\n" +
                            "    \"password\": \"$2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PostMapping
    public ResponseEntity<BaseResponse<CompanyCreateRequest>> createCompany(@RequestBody CompanyCreateRequest request) {

        companyService.createCompany(request);

        return ResponseBuilder.generateResponse(
                "Create company successfully!",
                HttpStatus.CREATED,
                request
        );
    }


    @Operation(summary = "Update a company", description = "[Admin] Update a company and its information")
    @Parameter(name = "company-id", description = "A specific company ID", in = ParameterIn.PATH, required = true, example = "4")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields Request", required = true, content = @Content(
            examples = {
                    @ExampleObject(name = "Update status", value = "{\n" +
                            "  \"status\": \"Đang hoạt động\"\n" +
                            "}")}))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(value = "{\n" +
                            "    \"id\": \"2\",\n" +
                            "    \"username\": \"manager-1\",\n" +
                            "    \"password\": \"$2a$10$/dH1LFY1VeSe9aQoibV8puAthiOjM/7Cb0NwnDfSA40wUxnagEkRG\",\n" +
                            "    \"status\": \"Đang hoạt động\"\n" +
                            "  }")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @PatchMapping("/{company-id}")
    public ResponseEntity<BaseResponse<CompanyHasStatusResponse>> updateCompany(
            @PathVariable("company-id") Integer id,
            @RequestBody Map<String, Object> fields) {

        CompanyHasStatusResponse response = companyService.updateCompany(id, fields);

        return ResponseBuilder.generateResponse(
                "Update company successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Get all companies", description = "[Admin] Get a list of all companies")
    @Parameters(value = {
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by Id. Multiple sort criteria are supported.",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Get all companies", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 11,\n" +
                            "      \"name\": \"Dịch vụ vệ sinh bất ổn\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fvesinh-1249.png?alt=media&token=d62144ea-6e2d-4f52-84b9-8eba78fe7b73\",\n" +
                            "      \"address\": \"48 Binh Hung Hoa\",\n" +
                            "      \"ward\": \"Bình Hưng Hòa B\",\n" +
                            "      \"district\": \"Bình Tân\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"0909900999\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 10,\n" +
                            "      \"name\": \"Dịch vụ vệ sinh quận Tân Phú\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload%20(1).jpg?alt=media&token=a7c06b54-9abb-4468-8abe-38b9431febba\",\n" +
                            "      \"address\": \"28 Hiền Vương\",\n" +
                            "      \"ward\": \"Phú Thạnh\",\n" +
                            "      \"district\": \"Tân Phú\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19008682\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 9,\n" +
                            "      \"name\": \"Đơn vị vệ sinh quận 6\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload%20(1).jpg?alt=media&token=2be6cb02-e666-482b-b360-3024553ba67a\",\n" +
                            "      \"address\": \"218 Nguyễn Văn Luông\",\n" +
                            "      \"ward\": \"Phường 12\",\n" +
                            "      \"district\": \"Quận 6\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19008787\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 6,\n" +
                            "      \"name\": \"Dịch vụ công ích quận 6\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload.jpg?alt=media&token=71577211-00d8-49f2-b3b4-68cdc060c0c3\",\n" +
                            "      \"address\": \"232 Nguyễn Văn Luông\",\n" +
                            "      \"ward\": \"Phường 11\",\n" +
                            "      \"district\": \"Quận 6\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19006088\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 4,\n" +
                            "      \"name\": \"Hieu company 190423\",\n" +
                            "      \"logo\": \"https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing\",\n" +
                            "      \"address\": \"Lô E2a-7, Đường D1\",\n" +
                            "      \"ward\": \"Phường Long Thạnh Mỹ\",\n" +
                            "      \"district\": \"Quận Thủ Đức\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"phone\": \"(028) 1111 2222\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"name\": \"Công ty dịch vụ công ích quận 1\",\n" +
                            "      \"logo\": \"https://dichvucongichquan1.com/wp-content/uploads/2021/12/logo.svg\",\n" +
                            "      \"address\": \"28-30 Nguyễn Thái Bình\",\n" +
                            "      \"ward\": \"Phường Nguyễn Thái Bình\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"phone\": \"(028) 38.215.611\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping
    public ResponseEntity<BaseResponse<List<CompanyHasStatusResponse>>> getAllCompanies(
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<CompanyHasStatusResponse> responses = companyService.getAllCompanies(paginationRequest);

        return ResponseBuilder.generateResponse(
                "Get list of all companies successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of all companies", description = "[Admin] Count list of company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {@ExampleObject(value = "10")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping(value = "/count")
    public ResponseEntity<BaseResponse<Integer>> count() {

        int response = companyService.count();

        return ResponseBuilder.generateResponse(
                "Count list of company successfully!",
                HttpStatus.OK,
                response
        );
    }

    @Operation(summary = "Search companies", description = "[Admin] Search companies")
    @Parameters(value = {
            @Parameter(name = "search-text", description = "Search text", in = ParameterIn.QUERY, allowReserved = true),
            @Parameter(name = "sort",
                    in = ParameterIn.QUERY,
                    description = "Sorting criteria in the format: property(,asc|desc). Default sort order is descending by Id. Multiple sort criteria are supported.",
                    array = @ArraySchema(schema = @Schema(implementation = String.class), maxItems = 5),
                    allowReserved = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {
                    @ExampleObject(name = "Search companies", value = "[\n" +
                            "    {\n" +
                            "      \"id\": 11,\n" +
                            "      \"name\": \"Dịch vụ vệ sinh bất ổn\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fvesinh-1249.png?alt=media&token=d62144ea-6e2d-4f52-84b9-8eba78fe7b73\",\n" +
                            "      \"address\": \"48 Binh Hung Hoa\",\n" +
                            "      \"ward\": \"Bình Hưng Hòa B\",\n" +
                            "      \"district\": \"Bình Tân\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"0909900999\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 10,\n" +
                            "      \"name\": \"Dịch vụ vệ sinh quận Tân Phú\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload%20(1).jpg?alt=media&token=a7c06b54-9abb-4468-8abe-38b9431febba\",\n" +
                            "      \"address\": \"28 Hiền Vương\",\n" +
                            "      \"ward\": \"Phú Thạnh\",\n" +
                            "      \"district\": \"Tân Phú\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19008682\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 9,\n" +
                            "      \"name\": \"Đơn vị vệ sinh quận 6\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload%20(1).jpg?alt=media&token=2be6cb02-e666-482b-b360-3024553ba67a\",\n" +
                            "      \"address\": \"218 Nguyễn Văn Luông\",\n" +
                            "      \"ward\": \"Phường 12\",\n" +
                            "      \"district\": \"Quận 6\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19008787\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 6,\n" +
                            "      \"name\": \"Dịch vụ công ích quận 6\",\n" +
                            "      \"logo\": \"https://firebasestorage.googleapis.com/v0/b/toilet-map-img.appspot.com/o/logo%2Fdownload.jpg?alt=media&token=71577211-00d8-49f2-b3b4-68cdc060c0c3\",\n" +
                            "      \"address\": \"232 Nguyễn Văn Luông\",\n" +
                            "      \"ward\": \"Phường 11\",\n" +
                            "      \"district\": \"Quận 6\",\n" +
                            "      \"province\": \"Hồ Chí Minh\",\n" +
                            "      \"phone\": \"19006088\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 4,\n" +
                            "      \"name\": \"Hieu company 190423\",\n" +
                            "      \"logo\": \"https://drive.google.com/file/d/1qmWrHPZ6e-XA8NZtaT9UVWXRXwpMXXA2/view?usp=sharing\",\n" +
                            "      \"address\": \"Lô E2a-7, Đường D1\",\n" +
                            "      \"ward\": \"Phường Long Thạnh Mỹ\",\n" +
                            "      \"district\": \"Quận Thủ Đức\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"phone\": \"(028) 1111 2222\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": 2,\n" +
                            "      \"name\": \"Công ty dịch vụ công ích quận 1\",\n" +
                            "      \"logo\": \"https://dichvucongichquan1.com/wp-content/uploads/2021/12/logo.svg\",\n" +
                            "      \"address\": \"28-30 Nguyễn Thái Bình\",\n" +
                            "      \"ward\": \"Phường Nguyễn Thái Bình\",\n" +
                            "      \"district\": \"Quận 1\",\n" +
                            "      \"province\": \"Thành phố Hồ Chí Minh\",\n" +
                            "      \"phone\": \"(028) 38.215.611\",\n" +
                            "      \"status\": \"Đang hoạt động\"\n" +
                            "    }\n" +
                            "  ]")
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping(value = "/search")
    public ResponseEntity<BaseResponse<List<CompanyHasStatusResponse>>> searchCompanies(
            @RequestParam("search-text") String searchText,
            @ModelAttribute BasePaginationRequest paginationRequest) {

        List<CompanyHasStatusResponse> responses;

        if (searchText == null || searchText.equals("")) {
            responses = companyService.getAllCompanies(paginationRequest);
        } else {
            responses = companyService.searchCompany(searchText ,paginationRequest);
        }

        return ResponseBuilder.generateResponse(
                "Search companies successfully!",
                HttpStatus.OK,
                responses
        );
    }

    @Operation(summary = "Count list of search companies", description = "[Admin] Count list of search companies")
    @Parameter(name = "search-text", description = "Search text", in = ParameterIn.QUERY, allowReserved = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully!", content = @Content(examples = {@ExampleObject(value = "10")})),
            @ApiResponse(responseCode = "400", description = "Bad Request!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthenticated!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Unauthorized!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Resource Not Found!", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = OpenApiConfig.securitySchemeName)
    @RolesAllowed({RoleConstant.ADMIN})
    @GetMapping(value = "/search/count")
    public ResponseEntity<BaseResponse<Integer>> countSearchingCompanies(
            @RequestParam("search-text") String searchText) {

        int response = 0;

        if (searchText == null || searchText.equals("")) {
            response = companyService.count();
        } else {
            response = companyService.countSearchingCompanies(searchText);
        }

        return ResponseBuilder.generateResponse(
                "Count list of search companies successfully!",
                HttpStatus.OK,
                response
        );
    }
}
