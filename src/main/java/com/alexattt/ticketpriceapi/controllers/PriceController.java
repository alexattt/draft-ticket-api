package com.alexattt.ticketpriceapi.controllers;

import com.alexattt.ticketpriceapi.models.PricingRequest;
import com.alexattt.ticketpriceapi.models.PricingResponse;
import com.alexattt.ticketpriceapi.services.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price")
@Tag(name = "Ticket Pricing", description = "API endpoints for calculating ticket prices and service health checks")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping("/draft-ticket")
    @Operation(
            summary = "Calculate draft ticket price",
            description = "Calculate the pricing for a draft ticket based on passenger details, terminals, and purchase date"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Price calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PricingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or calculation error",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during price calculation"
            )
    })
    public ResponseEntity<PricingResponse> calculateDraftPrice(
            @Parameter(
                    description = "Pricing request containing passenger list, terminal information, and purchase date",
                    required = true,
                    schema = @Schema(implementation = PricingRequest.class)
            )
            @RequestBody PricingRequest request) {
        try {
            PricingResponse response = priceService.calculateDraftPrice(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("/api/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Pricing service is running");
    }
}