var mongoose = require('mongoose');

var templateItemsSchema = new mongoose.Schema({
    Name : String,
    Quantity : Number,
    Unit : String,
    TemplateId : Number
});

module.exports = mongoose.model('TemplateItems',templateItemsSchema);