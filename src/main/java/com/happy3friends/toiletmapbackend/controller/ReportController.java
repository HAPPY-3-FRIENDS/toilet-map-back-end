package com.happy3friends.toiletmapbackend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Report", description = "Report API")
@RestController
@RequestMapping(value = "/api/reports")
public class ReportController {


}
