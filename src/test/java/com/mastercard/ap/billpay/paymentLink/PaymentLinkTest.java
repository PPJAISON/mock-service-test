package com.mastercard.ap.billpay.paymentLink;

import static com.mastercard.ap.billpay.TestSuite.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.ap.billpay.dto.paymentlink.deletelink.DeleteLinkRes;
import com.mastercard.ap.billpay.dto.paymentlink.paymentlinkres.PaymentLinkResDTO;
import com.mastercard.ap.billpay.service.paymentlink.PaymentLinkService;
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
public class PaymentLinkTest {

  @Autowired private PaymentLinkService paymentLinkService;
  String initiatingPtyId = INITIATING_PARTY_ID;

  @Test
  public void paymentLinkTest() throws Exception {
    PaymentLinkResDTO jsonData = getPaymentLinkJSON(CREATE_PAYMENT_LINK_JSON);
    Assertions.assertNotNull(jsonData.getOriginalMessageId());
    Assertions.assertNotNull(jsonData.getInitiatingPtyId());
    Assertions.assertNotNull(jsonData.getMessageId());
    Assertions.assertNotNull(jsonData.getCreateDateTime());
    Assertions.assertNotNull(jsonData.getStatusInformation().getTransactionStatus());
    ResponseEntity<PaymentLinkResDTO> paymentLinkRes =
        paymentLinkService.createPaymentLink(initiatingPtyId);
    Assertions.assertEquals(HttpStatus.OK, paymentLinkRes.getStatusCode());
  }

  @Test
  public void deleteLinkTest() throws Exception {
    DeleteLinkRes deleteJson = getDeleteLinkJson(DELETE_PAYMENT_LINK_JSON);
    Assertions.assertNotNull(deleteJson.getClass());
    Assertions.assertNotNull(deleteJson.getOriginalMessageId());
    Assertions.assertNotNull(deleteJson.getInitiatingPtyId());
    Assertions.assertNotNull(deleteJson.getMessageId());
    Assertions.assertNotNull(deleteJson.getCreateDateTime());
    Assertions.assertNotNull(deleteJson.getStatusInformation());
    Assertions.assertNotNull(deleteJson.getCreditorInformation().getCreditorId());
    String schemeTxId = SCHEME_TX_ID;
    ResponseEntity<DeleteLinkRes> deletePaymentLink =
        paymentLinkService.deletePaymentLink(schemeTxId, initiatingPtyId);
    Assertions.assertEquals(HttpStatus.OK, deletePaymentLink.getStatusCode());
  }

  private PaymentLinkResDTO getPaymentLinkJSON(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, PaymentLinkResDTO.class);
  }

  private DeleteLinkRes getDeleteLinkJson(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, DeleteLinkRes.class);
  }
}
