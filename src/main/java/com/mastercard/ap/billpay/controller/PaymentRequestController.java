package com.mastercard.ap.billpay.controller;

import static com.mastercard.ap.billpay.common.Constants.*;

import com.mastercard.ap.billpay.dto.paymentrequest.paymentconfirmadvice.PaymentConfirmRes;
import com.mastercard.ap.billpay.service.paymentrequest.PaymentRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ZAPP_URL)
public class PaymentRequestController {
  @Autowired private PaymentRequestService paymentRequestService;

  // Payment Request Confirmation Mock
  @PostMapping(value = URL_PAYMENT_REQUEST_CONFIRMATION_ADVICE)
  public ResponseEntity<PaymentConfirmRes> paymentConfirmAdvice(
      @RequestHeader(PARTICIPANT_ID) String initiatingPtyId,
      @PathVariable(SCHEME_TX_ID) String schemeTxId) {
    return paymentRequestService.paymentConfirmationAdvice(schemeTxId, initiatingPtyId);
  }

  // Payment Request Document Retrieval Mock
  @GetMapping(value = URL_DOCUMENT_RETRIEVAL)
  public ResponseEntity<String> paymentRequestRetrieval(
      @PathVariable(PAYMENT_REQUEST_ID) String schemeTxId,
      @PathVariable(DOC_ID) String docId,
      @RequestHeader(PARTICIPANT_ID) String initiatingPtyId) {
    return paymentRequestService.getDocument();
  }
}
