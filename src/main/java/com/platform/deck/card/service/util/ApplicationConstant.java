package com.platform.deck.card.service.util;

public class ApplicationConstant {

    //    HTTP code
    public static final String CODE_200 = "200";
    public static final String CODE_201 = "201";
    public static final String CODE_202 = "202";
    public static final String CODE_400 = "400";
    public static final String CODE_401 = "401";
    public static final String CODE_404 = "404";
    public static final String CODE_500 = "500";

    //    FTTP description
    public static final String CODE_200_DESC = "Operation successful";
    public static final String CODE_201_DESC = "Resource created successfully";
    public static final String CODE_202_DESC = "Request accepted and processing (Saga started)";
    public static final String CODE_400_DESC = "Bad Request - Validation failed";
    public static final String CODE_401_DESC = "Unauthorized - Authentication failed";
    public static final String CODE_404_DESC = "Not Found - Resource does not exist";
    public static final String CODE_500_DESC = "Internal Server Error - Unexpected failure";

    public static final String HEADER_VALIDATION = "X-User-Id must be a valid numeric MySQL ID";
}
