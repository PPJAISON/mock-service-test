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

package com.mastercard.ap.billpay.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String ZAPP_URL = "/zapp/bpx/v1";
  public static final String URL_PAYMENT_LINK_CREATE = "/payment-links";
  public static final String URL_PAYMENT_LINK_DELETE = "/payment-links/{scheme_tx_id}";
  public static final String URL_PAYMENT_REQUEST_CONFIRMATION_ADVICE =
      "/payment-requests/{scheme_tx_id}/advices";
  public static final String URL_DOCUMENT_RETRIEVAL =
      "/payment-requests/{payment_request_id}/docs/{doc_id}/uris";

  public static final String SCHEME_TX_ID = "scheme_tx_id";
  public static final String LINKING_REF = "LinkingReference";
  public static final String PARTICIPANT_ID = "X-Participant-ID";
  public static final String PAYMENT_REQUEST_ID = "payment_request_id";
  public static final String ACCOUNT_SERVICE_REFERENCE = "AccountServiceReferenceNumber:";
  public static final String CREATE_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  public static final String DOC_ID = "doc_id";
  public static final String DOCUMENT_URI_NAME = "documentURI";
  public static final String DOCUMENT_URI =
      "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";

  public static final String MOCK_PAYMENT_LINK_CREATE = "${mock.payment-link.create}";
  public static final String MOCK_PAYMENT_LINK_DELETE = "${mock.payment-link.delete}";
  public static final String MOCK_PAYMENT_REQUEST_CONFIRM = "${mock.payment-request.confirm}";
  public static final String MOCK_DOCUMENT_RETRIEVAL = "${mock.payment-request.retrieval}";
  public static final String CREATE_DATE_TIME_FORMAT_PAYMENTLINK = "yyyy-mm-ddThh:mm:ssz";
}
