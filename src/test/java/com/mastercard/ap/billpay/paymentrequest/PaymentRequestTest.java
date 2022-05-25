package com.mastercard.ap.billpay.paymentrequest;

import static com.mastercard.ap.billpay.TestSuite.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.ap.billpay.dto.paymentrequest.paymentconfirmadvice.PaymentConfirmRes;
import com.mastercard.ap.billpay.service.paymentrequest.PaymentRequestService;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public class PaymentRequestTest {

  @Autowired private PaymentRequestService paymentRequestService;

  @Test
  public void paymentConfirmationAdviceTest() throws Exception {
    PaymentConfirmRes paymentConfirmResJson =
        getPaymentConfirmAdviceJSON(PAYMENT_CONFIRMATION_ADVICE_JSON);
    Assertions.assertNotNull(paymentConfirmResJson.getMessageInformation().getMessageId());
    Assertions.assertNotNull(paymentConfirmResJson.getMessageInformation().getCreateDateTime());
    Assertions.assertNotNull(paymentConfirmResJson.getMessageInformation().getOriginalMessageId());
    Assertions.assertNotNull(paymentConfirmResJson.getMessageInformation().getInitiatingPtyId());
    Assertions.assertNotNull(paymentConfirmResJson.getStatusInformation().getTransactionStatus());
    Assertions.assertNotNull(
        paymentConfirmResJson.getStatusInformation().getAdditionalInformation());
    Assertions.assertNotNull(
        paymentConfirmResJson
            .getOriginalTransactionInformation()
            .getOriginalPaymentInformationId());
    Assertions.assertNotNull(
        paymentConfirmResJson.getOriginalTransactionInformation().getOriginalEndToEndId());
    String initiatingPtyId = INITIATING_PARTY_ID;
    String schemeTxId = SCHEME_TX_ID;
    ResponseEntity<PaymentConfirmRes> paymentLinkRes =
        paymentRequestService.paymentConfirmationAdvice(schemeTxId, initiatingPtyId);
    Assertions.assertEquals(HttpStatus.OK, paymentLinkRes.getStatusCode());
  }

  @Test
  public void documentRetrievalTest() {
    ResponseEntity<String> paymentLinkRes = paymentRequestService.getDocument();
    Assertions.assertEquals(HttpStatus.OK, paymentLinkRes.getStatusCode());
  }

  private PaymentConfirmRes getPaymentConfirmAdviceJSON(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, PaymentConfirmRes.class);
  }
}
