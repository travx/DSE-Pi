<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
      "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
  <title>Real-time Dashboard</title>
  <meta name="keywords" content="">
  <meta name="description" content="">
  <link rel="stylesheet" type="text/css" href="default.css">
    <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.system.js"></script>
    <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.coreVector.js"></script>
     <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.animation.js"></script>  
    <script src="scripts/jquery-latest.min.js"></script>  
    
<script type="text/javascript">

var chart_summary;
var chart_vendorsummary;
var chart_popcorn;
var chart_magnolia;

function loadPage(){
	loadSummary();
	loadVendorSummary();
	loadPopcorn();
	loadMagnolia();
	new_data();
}

function loadSummary(){	
	chart_summary = new cfx.Chart();
	chart_summary.getAnimations().getLoad().setEnabled(true);
	chart_summary.setGallery(cfx.Gallery.Bar);  

	var axisY2 = chart_summary.getAxisY2();
	var order_series = chart_summary.getSeries().getItem(0);
	order_series.setAxisY(axisY2);	
	
	var divHolder = document.getElementById('Summary');
	chart_summary.create(divHolder);
}

function loadVendorSummary(){	
	chart_vendorsummary = new cfx.Chart();
	chart_vendorsummary.getAnimations().getLoad().setEnabled(true);
	chart_vendorsummary.setGallery(cfx.Gallery.Bar);  

	var axisY2 = chart_vendorsummary.getAxisY2();
	var order_series = chart_vendorsummary.getSeries().getItem(0);
	order_series.setAxisY(axisY2);	
	
	var divHolder = document.getElementById('VendorSummary');
	chart_vendorsummary.create(divHolder);
}

function loadPopcorn(){	
	chart_popcorn = new cfx.Chart();
	chart_popcorn.getAnimations().getLoad().setEnabled(true);
	chart_popcorn.setGallery(cfx.Gallery.Lines);  
	
	var fields = chart_popcorn.getDataSourceSettings().getFields();
	var field1 = new cfx.FieldMap();
	var field2 = new cfx.FieldMap();
	var field3 = new cfx.FieldMap();
	
	field1.setName("seconds");
	field1.setDisplayName("Time");
	field1.setUsage(cfx.FieldUsage.Label);
	fields.add(field1);
	
	field2.setName("revenue");
	field2.setDisplayName("Revenue");
	field2.setUsage(cfx.FieldUsage.Value);
	fields.add(field2);
	
	field3.setName("profit");
	field3.setDisplayName("Profit");
	field3.setUsage(cfx.FieldUsage.Value);
	fields.add(field3);	
	
	var divHolder = document.getElementById('Popcorn');
	chart_popcorn.create(divHolder);
}

function loadMagnolia(){	
	chart_magnolia = new cfx.Chart();
	chart_magnolia.getAnimations().getLoad().setEnabled(true);
	chart_magnolia.setGallery(cfx.Gallery.Lines);
	
	var fields = chart_magnolia.getDataSourceSettings().getFields();
	var field1 = new cfx.FieldMap();
	var field2 = new cfx.FieldMap();
	var field3 = new cfx.FieldMap();
	
	field1.setName("seconds");
	field1.setDisplayName("Time");
	field1.setUsage(cfx.FieldUsage.Label);
	fields.add(field1);
	
	field2.setName("revenue");
	field2.setDisplayName("Revenue");
	field2.setUsage(cfx.FieldUsage.Value);
	fields.add(field2);
	
	field3.setName("profit");
	field3.setDisplayName("Profit");
	field3.setUsage(cfx.FieldUsage.Value);
	fields.add(field3);		
	
	var divHolder = document.getElementById('Magnolia');
	chart_magnolia.create(divHolder);
}


function new_data(){
    $.getJSON('TrackerServlet?action=summary', function(data) {
    	chart_summary.setDataSource(data);
        });
    $.getJSON('TrackerServlet?action=vendorsummary', function(data) {
    	chart_vendorsummary.setDataSource(data);
        });
    $.getJSON('TrackerServlet?action=vendor&vendor=Better%20Brews', function(data) {
    	chart_popcorn.setDataSource(data);
        });
    $.getJSON('TrackerServlet?action=vendor&vendor=Worldly%20Wines', function(data) {
    	chart_magnolia.setDataSource(data);
        });
}


setInterval(function(){new_data()}, 5000);


</script>        
    
</head>

<body onload="loadPage()">

<div id="upbg">
</div>

<div id="outer">

<div id="header">

<div id="headercontent">
<h1>Order Tracker</h1>
</div>
</div>


<div id="headerpic">
</div>

<div id="menu">
<!-- HINT: Set the class of any menu link below to "active" to make it appear active -->
<ul>
  <li><a href="index.jsp" class="active">Overview</a></li>
  <li><a href="http://appcontrol:8888/">OpsCenter</a></li>
</ul>
</div>

<div id="menubottom">
</div>

<div id="content">
<!-- Normal content: Stuff that's not going to be put in the left or right column. -->

<div id="normalcontent">
<h3><strong>Order Summary</strong></h3>

<div class="contentarea">
<!-- Normal content area start -->
<p>
<div id="Summary" style="width:400px;height:300px;display:inline-block"></div>
<div id="VendorSummary" style="width:500px;height:300px;display:inline-block"></div>
</p>

<h3><strong>Better Brews</strong></h3> <a href="vendor.jsp?vendor=Better%20Brews">View Products</a>
<p>
<div id="Popcorn" style="width:900px;height:300px;display:inline-block"></div>
</p>

<h3><strong>Worldly Wines</strong></h3> <a href="vendor.jsp?vendor=Worldly%20Wines">View Products</a>
<p>
<div id="Magnolia" style="width:900px;height:300px;display:inline-block"></div>
</p>

<p>&nbsp;</p>


<!-- Normal content area end -->
</div>
</div>
</div>

<div id="footer">

<div class="right">
Travis Price</div>
</div>
</div>
</body>
</html>
