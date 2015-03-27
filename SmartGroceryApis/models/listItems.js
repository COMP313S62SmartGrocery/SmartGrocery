var mongoose = require('mongoose');

var listItemsSchema = new mongoose.Schema({
    _ID : Number,
    Name : String,
    Quantity : Number,
    Unit : String,
    LastModified : Date,
    Reminder : Date,
    ListId : Number
});

var ListItems = mongoose.model('listItems',listItemsSchema);

module.exports = ListItems;