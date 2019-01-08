//import libraries
var express = require('express');
var bodyParser = require('body-parser');
var mongo = require('mongodb');
var serverless = require('serverless-http');
var monk = require('monk');
var path = require('path');
var http = require('http').Server(app);

//create necessary objects
var app = express();
var router = express.Router();


//you need to update wp with your own database name
var db = monk('mongodb://imhikarucat:12345abcde@ds149984.mlab.com:49984/whereisrain');

//use objects in app
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(function(req,res,next){
    req.db = db;
    next();
});

////////////////
app.use('/', router);

//get all
router.get('/rains', function(req, res) {
    req.db.collection('rains').find({},{"limit": 100},function(e,docs){
        res.json(docs);
    });
});

//get by _id
router.get('/rains/:id', function(req, res){
	req.db.collection('rains').findOne(req.params.id, function(e, doc){
		res.json(doc);
	})
});

//update (by _id)
router.put('/rains/:id', function(req, res){
	req.db.collection('rains').update(
		{_id: req.params.id}, 
		{
			latitude: req.body.latitude,
			longitude: req.body.longitude,
			condition: req.body.condition,
			time: req.body.time,
		});
	req.db.collection('rains').findOne(req.params.id, function(e, doc){
		res.json(doc);
	})

});

//delete (by _id)
router.delete('/rains/:id', function(req, res){
	req.db.collection('rains').remove({_id: req.params.id}, function(e, doc){
		res.json(doc);
	})
});

//create
router.post('/rains', function(req, res){
	req.db.collection('rains').insert(req.body, function(e, docs){
		res.json(docs);
	});
});
////////////////

app.set( 'port', ( process.env.PORT || 8080 ));

// Start node server
app.listen( app.get( 'port' ), function() {
  console.log( 'Node server is running on port ' + app.get( 'port' ));
});

module.exports.handler = serverless(app);

