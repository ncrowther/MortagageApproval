'use strict';

var utils = require('../utils/writer.js');

var AiGen = require('../service/AiGenService.js');

module.exports.generateAI = function generateAI(req, res, next, body, apiKey, income, loanAmount, creditScore, propertyValue) {

  console.log('apiKey: ', apiKey);
  console.log('Income: ', income);
  console.log('LoanAmount: ', loanAmount);
  console.log('Credit score: ', creditScore);
  console.log('Property Value: ', propertyValue);

  AiGen.getBearer(req, apiKey)
    .then(function (bearerTokenResponse) {
      console.log('****bearerTokenResponse: ', bearerTokenResponse);
      AiGen.generateAI(body, bearerTokenResponse, income, loanAmount, creditScore, propertyValue)
        .then(function (watsonxResponse) {
          console.log('****OK: ');
          utils.writeJson(res, watsonxResponse);
        })
        .catch(function (watsonxResponse) {
          console.log('****ERROR: ' + watsonxResponse);
          utils.writeJson(res, watsonxResponse);
        });
    })
    .catch(function (bearerTokenResponse) {
      console.log('****bearer error: ', bearerTokenResponse);
      utils.writeJson(res, bearerTokenResponse);
    });
};
