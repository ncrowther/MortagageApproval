/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.travels.ml.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.acme.travels.ml.json.JsonUtils;
import org.apache.commons.codec.binary.Base64;

public class MlApi {

    public static boolean debug = true;

    public static void ignoreSSL() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }
        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier validHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
        // All hosts will be valid
        HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
    }

    public static MlResponse invokeMlModelAndWait(String baseUrl, String tenantId, String username, String password,
            String processName, MlRequest payload, Integer waitSeconds)
            throws MlApiException, InterruptedException {

        final int RETRY_COUNT = 10;
        final int RETRY_IN_MILISECS = 3000;

        System.out.println("Call ML and wait...");

        //String bearerToken = getBearerToken(baseUrl, tenantId, username, password);

        // if (bearerToken == null) {
        //   throw new MlApiException(
        //            "Failed to login  to tenant +  " + tenantId + " with username " + username);
        //}

        String response = "";

        try {

            String income = payload.getSalary().toString();
            String loanAmount = payload.getLoan().toString();
            String propertyValue = payload.getPropertyValue().toString();
            String creditScore = "2000";

            response = invokeMlModel(baseUrl, tenantId, income, loanAmount, creditScore, propertyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if (response.code != 200) {
        //   mlResponse = new MlResponse(false, false);
        //}

        Integer responseCode = 200;
        Boolean approved = JsonUtils.getBoolean("approved", response);
        MlResponse mlResponse = new MlResponse(responseCode, approved);

        System.out.println("mlResponse: " + mlResponse);

        return mlResponse;
    }

    public static String doRest(String command, String urlString, String content, HashMap<String, String> headerMap,
            String userid, String password) throws Exception {
        if (debug) {
            System.out.println(">> doRest: command=" + command + ", urlString=" + urlString + ", content=" + content
                    + ", userid=" + userid + ", password=" + password);
        }

        if ((!command.equals("GET")) && (!command.equals("POST")) && (!command.equals("PUT"))) {
            throw new MlApiException("Unsupported command: " + command + ".  Supported commands are GET, POST, PUT");
        }

        URL url = new URL(urlString);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setRequestMethod(command);

        if (headerMap != null) {
            Set keySet = headerMap.keySet();
            Iterator it = keySet.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                httpUrlConnection.addRequestProperty(key, headerMap.get(key));
            }
        }

        if ((userid != null) && (!userid.isEmpty())) {
            String authorization = "Basic " + new String(Base64.encodeBase64(
                    new String(new StringBuilder(String.valueOf(userid)).append(":").append(password).toString())
                            .getBytes()));
            httpUrlConnection.setRequestProperty("Authorization", authorization);
        }

        if ((content != null) && ((command.equals("POST")) || (command.equals("PUT")))) {
            httpUrlConnection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(httpUrlConnection.getOutputStream());
            wr.write(content);
            wr.flush();
            wr.close();
        }

        InputStreamReader inReader = new InputStreamReader(httpUrlConnection.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inReader);
        StringBuffer sb = new StringBuffer();
        String line = bufferedReader.readLine();
        while (line != null) {
            sb.append(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();

        httpUrlConnection.disconnect();

        if (debug) {
            System.out.println("doRest: result is " + sb.toString());
        }
        return sb.toString();
    }

    public static class MlApiException extends Exception {
        private static final long serialVersionUID = 8768678L;

        public MlApiException(String arg0) {
            super();
        }
    }

    private static String postEncoded(String urlString, String content, String tenantId, HashMap<String, String> params)
            throws Exception {

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");

            String urlParameters = getDataString(params);

            httpUrlConnection.addRequestProperty("tenantId", tenantId);

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setInstanceFollowRedirects(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("charset", "utf-8");
            httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            httpUrlConnection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(httpUrlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            InputStreamReader inReader = new InputStreamReader(httpUrlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inReader);
            StringBuffer sb = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            httpUrlConnection.disconnect();

            if (debug) {
                System.out.println("doRest: result is " + sb.toString());
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new MlApiException("doRest exception: " + e.toString());
        }
    }

    private static String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    private static String invokeMlModel(String baseUrl, String apiKey,
            String income, String loanAmount, String creditScore, String propertyValue) throws Exception {

        String payload = "{\"tmp\": \"test\"}";
        String mlResponse = null;

        String mlUrl =
                baseUrl + "?apiKey=" + apiKey +
                        "&income=" + income +
                        "&loanAmount=" + loanAmount +
                        "&creditScore=" + creditScore +
                        "&propertyValue=" + propertyValue;

        HashMap<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Authorization", "Basic 123");
        headerMap.put("Content-Type", "application/json");

        String response = doRest("POST", mlUrl, payload, headerMap, null, null);

        System.out.println(response);

        return response;
    }

    public static void main(String args[]) {

        String baseUrl = "http://localhost:8080/mortgageapproval";
        String projectId = "09a51376-66e0-4a09-b49a-bf4902c3939f";
        String apiKey = "TZhK0Le3y3fagLMkb-lg_2ONLEd7XbLJ0qM8-D3lK76V";
        String income = "90000";
        String loanAmount = "200000";
        String creditScore = "2000";
        String propertyValue = "400000";
        MlRequest mlRequest = new MlRequest();
        final int WAIT_SECS = 30;

        try {

            MlResponse result = invokeMlModelAndWait(baseUrl, apiKey, "", "", "", mlRequest, WAIT_SECS);

            Boolean approved = result.getApproved();

            System.out.println("approved:" + approved);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
