package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static com.platform.deck.card.service.util.ApplicationConstant.*;

@ApiResponses(value = {

        @ApiResponse(responseCode = CODE_200, description = CODE_200_DESC),
        @ApiResponse(responseCode = CODE_201, description = CODE_201_DESC),
        @ApiResponse(responseCode = CODE_202, description = CODE_202_DESC),
        @ApiResponse(responseCode = CODE_400, description = CODE_400_DESC,
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = CODE_401, description = CODE_401_DESC,
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = CODE_404, description = CODE_404_DESC,
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESC,
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public abstract class BaseApiController {
}
