var mongoose = require('mongoose');

var listsSchema = new mongoose.Schema({
    _ID : Number,
    Name : String,
    Color : String,
    LastModified : Date
});

var Lists = mongoose.model('lists',listsSchema);

module.exports = Lists;