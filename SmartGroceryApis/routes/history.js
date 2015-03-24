var express = require('express');
var router = express();


router.get('/', function(req, res){
    //getting input from body parameters
    var itemName = req.body.name;
    
    //fetching history related to item from db
    var history = {};
    
    return JSON.stringify(history);
});

router.post('/', function(req, res){
    //getting item related history from body parameters
    var itemHistory = req.body.itemHistory;
    
    //code to add history related to item into db
    
    return JSON.stringify({done:'true'});
});

router.delete('/', function(req, res){
    //getting input from body parameters
    var itemName = req.body.name;
    
    //code to delete history related to item from db
    
    return JSON.stringify({done:'true'});
});

module.exports = router;