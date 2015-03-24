var express = require('express');
var router = express();


router.get('/', function(req, res){
    //getting input from body parameters
    var templateId = req.body.templateId;
    
    //code to fetch template data from db
    var template = {};
    
    return JSON.stringify(template);
});

router.post('/', function(req, res){
     //getting input from body parameters
    var template = req.body.template;
    
    //code to add template into database
    
    return JSON.stringify({ done : 'true'}); 
});

router.delete('/', function(req, res){
    //getting input from body parameters
    var templateId = req.body.templateId;
    
    //code to delete template from db
    
    return JSON.stringify({ done : 'true'}); 
});

module.exports = router;