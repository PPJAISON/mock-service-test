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

package com.mastercard.ap.billpay.dto.paymentlink.paymentlinkres;

import static com.mastercard.ap.billpay.common.Constants.CREATE_DATE_TIME_FORMAT_PAYMENTLINK;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentLinkResDTO {

  private String initiatingPtyId;

  private String messageId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CREATE_DATE_TIME_FORMAT_PAYMENTLINK)
  private String createDateTime;

  private String originalMessageId;

  private TransactionIdentification transactionIdentification;

  private StatusInformation statusInformation;
}
