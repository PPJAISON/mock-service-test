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
package com.mastercard.ap.billpay.controller;

import static com.mastercard.ap.billpay.common.Constants.*;

import com.mastercard.ap.billpay.dto.paymentlink.deletelink.DeleteLinkRes;
import com.mastercard.ap.billpay.dto.paymentlink.paymentlinkres.PaymentLinkResDTO;
import com.mastercard.ap.billpay.service.paymentlink.PaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ZAPP_URL)
public class PaymentLinkController {

  @Autowired private PaymentLinkService paymentLinkService;

  // Payment Link Create Mock
  @PostMapping(value = URL_PAYMENT_LINK_CREATE)
  public ResponseEntity<PaymentLinkResDTO> createPaymentLink(
      @RequestHeader(PARTICIPANT_ID) String initiatingPtyId) {
    return paymentLinkService.createPaymentLink(initiatingPtyId);
  }

  // Delete Link Mock
  @PutMapping(value = URL_PAYMENT_LINK_DELETE)
  public ResponseEntity<DeleteLinkRes> deleteLink(
      @PathVariable(SCHEME_TX_ID) String schemeTxId,
      @RequestHeader(PARTICIPANT_ID) String initiatingPtyId) {
    return paymentLinkService.deletePaymentLink(schemeTxId, initiatingPtyId);
  }
}
