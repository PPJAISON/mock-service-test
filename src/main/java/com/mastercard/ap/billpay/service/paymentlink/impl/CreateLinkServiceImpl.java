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

package com.mastercard.ap.billpay.service.paymentlink.impl;

import static com.mastercard.ap.billpay.common.Constants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.ap.billpay.common.Utils;
import com.mastercard.ap.billpay.dto.paymentlink.deletelink.DeleteLinkRes;
import com.mastercard.ap.billpay.dto.paymentlink.paymentlinkres.PaymentLinkResDTO;
import com.mastercard.ap.billpay.service.paymentlink.PaymentLinkService;
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
public class CreateLinkServiceImpl implements PaymentLinkService {

  @Value(MOCK_PAYMENT_LINK_CREATE)
  private String paymentLinkCreate;

  @Value(MOCK_PAYMENT_LINK_DELETE)
  private String paymentLinkDelete;

  public ResponseEntity<PaymentLinkResDTO> createPaymentLink(String initiatingPtyId) {
    ResponseEntity<PaymentLinkResDTO> linkBpxResponse = null;

    try {
      SimpleDateFormat output = new SimpleDateFormat(CREATE_DATE_TIME_FORMAT);
      String date = output.format(new Date());
      PaymentLinkResDTO jsonData = getPaymentLinkJSON(paymentLinkCreate);
      linkBpxResponse = new ResponseEntity<>(jsonData, HttpStatus.OK);
      PaymentLinkResDTO responseBody = linkBpxResponse.getBody();
      responseBody
          .getTransactionIdentification()
          .setLinkingReference(Utils.generateLinkingReference());
      responseBody.getTransactionIdentification().setSchemeTxId(Utils.generateSchemeTxId());
      responseBody.setCreateDateTime(date);
      responseBody.setInitiatingPtyId(initiatingPtyId);
      log.info(
          SCHEME_TX_ID
              + ":"
              + responseBody.getTransactionIdentification().getSchemeTxId()
              + "  "
              + LINKING_REF
              + ":"
              + responseBody.getTransactionIdentification().getLinkingReference());
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
    return linkBpxResponse;
  }

  public ResponseEntity<DeleteLinkRes> deletePaymentLink(
      String schemeTxId, String initiatingPtyId) {

    ResponseEntity<DeleteLinkRes> deleteLinkRes = null;
    try {
      SimpleDateFormat output = new SimpleDateFormat(CREATE_DATE_TIME_FORMAT);
      String date = output.format(new Date());

      DeleteLinkRes jsonData = getDeleteLinkJson(paymentLinkDelete);
      deleteLinkRes = new ResponseEntity<>(jsonData, HttpStatus.OK);
      deleteLinkRes.getBody().getTransactionIdentification().setSchemeTxId(schemeTxId);
      deleteLinkRes.getBody().setInitiatingPtyId(initiatingPtyId);
      deleteLinkRes.getBody().setCreateDateTime(date);
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }

    return deleteLinkRes;
  }

  private DeleteLinkRes getDeleteLinkJson(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, DeleteLinkRes.class);
  }

  private PaymentLinkResDTO getPaymentLinkJSON(String fileName) throws IOException {
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    String content = new String(inputStream.readAllBytes());
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(content, PaymentLinkResDTO.class);
  }
}
