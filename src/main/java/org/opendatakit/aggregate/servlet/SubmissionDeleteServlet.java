/*
 * Copyright (C) 2011 University of Washington.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.aggregate.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opendatakit.aggregate.ContextFactory;
import org.opendatakit.aggregate.constants.ServletConsts;
import org.opendatakit.aggregate.exception.ODKFormNotFoundException;
import org.opendatakit.aggregate.submission.SubmissionKey;
import org.opendatakit.aggregate.process.DeleteSubmissions;
import org.opendatakit.common.persistence.exception.ODKDatastoreException;
import org.opendatakit.common.web.CallingContext;
import org.opendatakit.common.web.constants.HtmlConsts;

/**
 * Servlet to delete a submission by its id
 *
 *
 * @author frank@kapernikov.com
 * 
 */
public class SubmissionDeleteServlet extends ServletUtilBase {

  /**
   * Serial number for serialization
   */
  private static final long serialVersionUID = -5861240658170389989L;

  /**
   * URI from base
   */
  public static final String ADDR = "view/deleteSubmission";

  /**
   * Handler for HTTP Get request that responds with the XML
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {


    CallingContext cc = ContextFactory.getCallingContext(this, req);

    // verify parameters are present
    String keyString = getParameter(req, ServletConsts.FORM_ID);
    if (keyString == null) {
      sendErrorNotEnoughParams(resp);
      return;
    }
    SubmissionKey key = new SubmissionKey(keyString);

    List<SubmissionKey> kl = new LinkedList<SubmissionKey>();
    kl.add(key);


    // https://groups.google.com/forum/#!topic/opendatakit-developers/apE33LMmWZI
    DeleteSubmissions ds = new DeleteSubmissions(kl);
    try {
      ds.deleteSubmissions(cc);
      resp.setCharacterEncoding(HtmlConsts.UTF8_ENCODE);
      resp.setContentType(HtmlConsts.RESP_TYPE_PLAIN);
      addOpenRosaHeaders(resp);
      PrintWriter out = resp.getWriter();
      out.write("OK");
      return;
    } catch (ODKFormNotFoundException e) {
      odkIdNotFoundError(resp);
      errorRetreivingData(resp);
    } catch (ODKDatastoreException e) {
      e.printStackTrace();
      errorRetreivingData(resp);
    }

  }
}
