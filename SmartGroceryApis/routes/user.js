var express = require('express');
var router = express();
var uuidGenerator = require('node-uuid');

router.get('/', function(req, res){
    //getting details from request
    var username = req.body.username;
    var password = req.body.password;
    
    //code to generate session id for user
    var sid = uuidGenerator.v1();
    
    //code to add sid into db
    console.log(JSON.stringify(req.body));
    res.send('Generated SID: '+sid);
});

router.post('/', function(req, res){
    //getting details from request
    var username = req.body.username;
    var password = req.body.password;
    var isNew = req.body.isNew;
    
    console.log(req.body);
    
    if(isNew == 'true'){
        //code to generate uuid
        var uuid = uuidGenerator.v1();
        
        //code to add username, password, and uuid into db
        
        //res.send('Generated UUID: '+uuid);
    }else{
        //code to get UUID from db
        
        //res.send('Finding UUID from db!');
    }
    
    res.send(username+' '+password+' '+isNew);
});

router.put('/', function(req, res){
    var username = req.body.username;
    var password = req.body.password;
    var newPassword = req.body.newPassword
    
    //code to change password for user
    
    res.send('Password for '+username + 'changed from '+password+' to '+newPassword);
});

router.delete('/', function(req, res){
    var username = req.body.username;
    var password = req.body.password;
    
    //code to delete user from database
    
    res.send(username + 'with password:'+password+' deleted!');
});

module.exports = router;