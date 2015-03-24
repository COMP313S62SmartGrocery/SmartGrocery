//loading required libraries
var express = require('express');
var bodyParser = require('body-parser');

//initializing application
var app = express();

//setting path for static files
app.use(express.static(__dirname+'/public/'));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

//loading path handlers variables
var userPathHandler = require('./routes/user');
var templatePathHandler = require('./routes/template');
var listPathHandler = require('./routes/list');
var listItemPathHandler = require('./routes/listItem');
var historyPathHandler = require('./routes/history');

//setting path handlers for respective paths
app.use('/user', userPathHandler);
app.use('/template', templatePathHandler);
app.use('/list', listPathHandler);
app.use('/listItem', listItemPathHandler);
app.use('/history', historyPathHandler);

//starting server
app.listen(3000);
console.log("Server is running on port 3000!");

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

module.exports = app;