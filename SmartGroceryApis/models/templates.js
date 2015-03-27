var mongoose = require('mongoose');

mongoose.connect(require('./settings').connectionString);

//creating schema for templates
var templateSchema = new mongoose.Schema({
    Id : Number,
    Name : String,
    Color : String
});


module.exports = mongoose.model('Templates',templateSchema);

