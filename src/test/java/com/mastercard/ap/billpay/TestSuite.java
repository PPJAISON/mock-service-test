package com.mastercard.ap.billpay;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSuite {
  public static final String CREATE_PAYMENT_LINK_JSON = "/mock/PaymentLink.json";
  public static final String DELETE_PAYMENT_LINK_JSON = "/mock/DeleteLink.json";
  public static final String PAYMENT_CONFIRMATION_ADVICE_JSON = "/mock/PaymentConfirmAdvice.json";
  public static final String INITIATING_PARTY_ID = "010033";
  public static final String SCHEME_TX_ID = "DEJWS6VPZCPODL7SBF";
}
