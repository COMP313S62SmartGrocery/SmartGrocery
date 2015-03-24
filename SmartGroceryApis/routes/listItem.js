var express = require('express');
var router = express();


router.get('/', function(req, res){
    res.send('Path: /list\tMethod:/GET');
});

router.post('/', function(req, res){
    res.send('Path: /list\tMethod:/POST');
});

router.put('/', function(req, res){
    res.send('Path: /list\tMethod:/PUT');
});

router.delete('/', function(req, res){
    res.send('Path: /list\tMethod:/DELETE');
});

module.exports = router;