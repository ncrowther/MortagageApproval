'use strict';

var request = require('request');

/**
 * Get bearer token 
 *
 * returns Bearer Token given API Key
 **/
exports.getBearer = function(req, apiKey) {

  return new Promise(function(resolve, reject) {

    console.log("***getBearer");

   //var requestJson = req.headers.authorization;
   //console.log('*** Auth: ', requestJson);

   console.log('*** ApiKey: ', apiKey);

    var options = {
      'method': 'POST',
      'url': 'https://iam.cloud.ibm.com/identity/token',
      'headers': {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      form: {
        'grant_type': 'urn:ibm:params:oauth:grant-type:apikey',
        'apikey': apiKey
      }
    };
    
    request(options, function (error, response) {
      if (error) throw new Error(error);

      var responseJson = JSON.parse(response.body);
      console.log(responseJson);

      var accessToken = responseJson.access_token;

      console.log("Token: " + accessToken );

      resolve(accessToken);

    });

  });
}

/**
 * NTC Generate Question
 * Generate Question
 *
 * body Request QuestionGen prompt
 * projectId String Project Identifier for classification
 * apiKey String Api Key
 * min_new_tokens Integer min length of generated questions (optional)
 * max_new_tokens Integer max length of generated questions (optional)
 * returns Response
 **/
exports.generateAI = function(body,apiKey,income, loanAmount, creditScore, propertyValue) {

  return new Promise(function(resolve, reject) {

    var approved = false
    var bearerToken = "Bearer " + apiKey;
    console.log("*************generateAI" );

    var promptTemplate = body.prompt;

    console.log('Income: ', income);
    console.log('LoanAmount: ', loanAmount);
    console.log('Credit score: ', creditScore);
    console.log('Property Value: ', propertyValue);

    var inputPayload = {
      "input_data": [
          {
              "fields": [
                  "CITY",
                  "STATE",
                  "ID",
                  "NAME",
                  "STREET_ADDRESS",
                  "STATE_CODE",
                  "ZIP_CODE",
                  "EMAIL_ADDRESS",
                  "PHONE_NUMBER",
                  "GENDER",
                  "SOCIAL_SECURITY_NUMBER",
                  "EDUCATION",
                  "EMPLOYMENT_STATUS",
                  "MARITAL_STATUS",
                  "INCOME",
                  "APPLIEDONLINE",
                  "RESIDENCE",
                  "YRS_AT_CURRENT_ADDRESS",
                  "YRS_WITH_CURRENT_EMPLOYER",
                  "NUMBER_OF_CARDS",
                  "CREDITCARD_DEBT",
                  "LOANS",
                  "LOAN_AMOUNT",
                  "CREDIT_SCORE",
                  "CRM_ID",
                  "COMMERCIAL_CLIENT",
                  "COMM_FRAUD_INV",
                  "FORM_ID",
                  "PROPERTY_CITY",
                  "PROPERTY_STATE",
                  "PROPERTY_VALUE"
              ],
              "values": [
                  [
                      "Reading",      // City
                      "NY",           // State 
                      null,           // ID
                      null,           // Name
                      null,           // Street
                      null,           // State
                      null,           // Zip
                      null,           // Email 
                      null,           // Phone
                      "Male",         // Gender
                      null,           // SSN
                      "Bachelor",     // Education
                      "Employed",     // Employment Status
                      "Single",       // Marital Status
                      income,         // Income
                      null,           // Applied Online
                      "Private Renting",  // Residence
                      1,             // Years at current address
                      1,             // Years with current employer
                      2,              // Number of cards
                      10000,          // Creditcard debt
                      1,              // Loans
                      loanAmount,     // Loan amount
                      creditScore,    // Credit Score
                      null,           // CRM id
                      false,          // Commercial client
                      false,          // Fraud ind
                      null,           //  Form Id
                      "Reading",      // Property City
                      "NY",           // Property State
                      propertyValue   // Property Value 
                  ]
              ]
          }
      ]
  }
     
    var inputStr = JSON.stringify(inputPayload);
    console.log('***inputStr: ', inputStr);

    var options = {
      'method': 'POST',
      'url': 'https://us-south.ml.cloud.ibm.com/ml/v4/deployments/mortgageapprovalmodel/predictions?version=2021-05-01',
      'headers': {
        'Content-Type': 'application/json',
        'Authorization': bearerToken,
      },
      body: inputStr
    };

    request(options, function (error, response) {
      if (error) throw new Error(error);
      console.log(response.body);

      var responsePayload = {
        "generated" : "Cannot invoke ML service",
      };

      var responseJson = JSON.parse(response.body);

      console.log('***Response: ', responseJson);
      
      if (responseJson.status_code != 403) {

        var approvalFlag = responseJson.predictions[0].values[0][0]

        if (approvalFlag === 1) {
          approved = true
        } else {
          approved = false
        }

        console.log('***approved: ', approved);

        responsePayload = {
          "approved": approved
        }
      }

     resolve(responsePayload);

    });

  })
}

