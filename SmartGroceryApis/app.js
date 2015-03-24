//loading required libraries
var express = require('express');

//initializing application
var app = express();

app.use(express.static(__dirname+'/public/'));


app.get('/',function(req,res){
    res.send('Hello From API!');
});

//starting server
app.listen(3000);
console.log("Server is running on port 3000!");