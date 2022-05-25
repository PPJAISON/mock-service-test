/*
 * Copyright© 2021  Mastercard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mastercard.ap.billpay.service.paymentrequest.impl;

import static com.mastercard.ap.billpay.common.Constants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mastercard.ap.billpay.common.Utils;
import com.mastercard.ap.billpay.dto.paymentrequest.paymentconfirmadvice.PaymentConfirmRes;
import com.mastercard.ap.billpay.service.paymentrequest.PaymentRequestService;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentRequestServiceImpl implements PaymentRequestService {

  @Value(MOCK_PAYMENT_REQUEST_CONFIRM)
  private String paymentRequestConfirm;

  @Override
  public ResponseEntity<String> getDocument() {
    ResponseEntity<String> documentRetrievalRes = null;
    try {
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode doc = mapper.createObjectNode();
      doc.put(DOCUMENT_URI_NAME, DOCUMENT_URI);
      String jsonContent = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(doc);
      documentRetrievalRes = new ResponseEntity<>(jsonContent, HttpStatus.OK);
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
    return documentRetrievalRes;
  }

  public ResponseEntity<PaymentConfirmRes> paymentConfirmationAdvice(
      String schemeTxId, String initiatingPtyId) {
    ResponseEntity<PaymentConfirmRes> paymentConfirmRes = null;
    try {
      SimpleDateFormat output = new SimpleDateFormat(CREATE_DATE_TIME_FORMAT);
      String date = output.format(new Date());
      PaymentConfirmRes jsonData = getPaymentConfirmAdviceJSON(paymentRequestConfirm);
      paymentConfirmRes = new ResponseEntity<>(jsonData, HttpStatus.OK);
      paymentConfirmRes
          .getBody()
          .getOriginalTransactionInformation()
          .setAccountServicerReference(Utils.generateSchemeTxId());
      paymentConfirmRes.getBody().getMessageInformation().setInitiatingPtyId(initiatingPtyId);
      paymentConfirmRes.getBody().getMessageInformation().setCreateDateTime(date);
      paymentConfirmRes
          .getBody()
          .getOriginalTransactionInformation()
          .setOriginalPaymentInformationId(Utils.generateSchemeTxId());
      log.info(
          ACCOUNT_SERVICE_REFERENCE
              + paymentConfirmRes
                  .getBody()
                  .getOriginalTransactionInformation()
                  .getAccountServicerReference());
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
    return paymentConfirmRes;
  }

  private PaymentConfirmRes getPaymentConfirmAdviceJSON(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, PaymentConfirmRes.class);
  }
}
