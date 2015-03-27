var mongoose = require('mongoose');

var itemHistorySchema = new mongoose.Schema({
    _ID : Number,
    Name : String,
    Quantity : Number,
    Unit : String,
    Date : Date
});

var ItemHistory = mongoose.model('itemHistory',itemHistorySchema);

module.exports = ItemHistory;