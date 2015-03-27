var mongoose = require('mongoose');

var usersSchema = new mongoose.Schema({
    Username : String,
    Password : String, 
    AUTH_KEY : String,
    SESSION_KEY : { type : String, default : 'undefined' },
    SESSION_EXPIRY : Date
});

module.exports = mongoose.model('Users', usersSchema);