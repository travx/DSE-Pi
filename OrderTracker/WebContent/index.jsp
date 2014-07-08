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
    <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.advanced.js"></script>   
    <script src="scripts/jquery-latest.min.js"></script>  
    
<script type="text/javascript">

var chart_summary;
var chart_vendorsummary;
var chart_popcorn;
var chart_magnolia;
var revenue = [0,0];
var profit = [0,0];
var tot_revenue = [0,0];
var tot_profit = [0,0];
var vendor = ["Vendor1", "Vendor2"];

function loadPage(){
	initial_data();
	loadSummary();
	loadVendorSummary();
	loadPopcorn();
	loadMagnolia();
	//new_data();
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
	
	var axisY = chart_popcorn.getAxisY();
	axisY.setMin(0);
	axisY.setMax(15000);
	
	var data = chart_popcorn.getData();
	data.setPoints(20);
	data.setSeries(2);
	
	chart_popcorn.getSeries().getItem(0).setText("Revenue");
	chart_popcorn.getSeries().getItem(1).setText("Profit");

	var realTime = chart_popcorn.getRealTime();
	realTime.setBufferSize(20);
	realTime.setMode(cfx.RealTimeMode.Scroll);
	
	var divHolder = document.getElementById('Popcorn');
	chart_popcorn.create(divHolder);
}

function loadMagnolia(){	
	chart_magnolia = new cfx.Chart();
	chart_magnolia.getAnimations().getLoad().setEnabled(true);
	chart_magnolia.setGallery(cfx.Gallery.Lines);
	
	var axisY = chart_magnolia.getAxisY();
	axisY.setMin(0);
	axisY.setMax(15000);
	
	var data = chart_magnolia.getData();
	data.setPoints(20);
	data.setSeries(2);
	
	chart_magnolia.getSeries().getItem(0).setText("Revenue");
	chart_magnolia.getSeries().getItem(1).setText("Profit");	

	var realTime = chart_magnolia.getRealTime();
	realTime.setBufferSize(20);
	realTime.setMode(cfx.RealTimeMode.Scroll);	
	
	var divHolder = document.getElementById('Magnolia');
	chart_magnolia.create(divHolder);
}


function initial_data(){
    $.getJSON('TrackerServlet?action=vendorsummary', function(data) {
    	$.each(data, function(index, element) {
    		revenue[index] = element.revenue - tot_revenue[index];
    		tot_revenue[index] = element.revenue;
    		profit[index] = element.profit - tot_profit[index];
    		tot_profit[index] = element.profit;
    		vendor[index] = element.vendor;
    	});
    	
        $("#vendor1").append("<h3><strong>" + vendor[0] + "</strong></h3>");
        $("#vendor1").append("<a href=vendor.jsp?vendor=" + encodeURIComponent(vendor[0]) + ">View Products</a>");
        
        $("#vendor2").append("<h3><strong>" + vendor[1] + "</strong></h3>");
        $("#vendor2").append("<a href=vendor.jsp?vendor=" + encodeURIComponent(vendor[1]) + ">View Products</a>");
        
    }); 	
}


function new_data(){
    $.getJSON('TrackerServlet?action=summary', function(data) {
    	chart_summary.setDataSource(data);
        });
    $.getJSON('TrackerServlet?action=vendorsummary', function(data) {
    	chart_vendorsummary.setDataSource(data);
    	$.each(data, function(index, element) {
    		revenue[index] = element.revenue - tot_revenue[index];
    		tot_revenue[index] = element.revenue;
    		profit[index] = element.profit - tot_profit[index];
    		tot_profit[index] = element.profit;
    	});
    	chart_popcorn.getRealTime().beginAddData(1, cfx.RealTimeAction.Append);
    	chart_popcorn.getData().setItem(0,0,revenue[0]);
    	chart_popcorn.getData().setItem(1,0,profit[0]);
    	chart_popcorn.getRealTime().endAddData(false,false);
    	
    	chart_magnolia.getRealTime().beginAddData(1, cfx.RealTimeAction.Append);
    	chart_magnolia.getData().setItem(0,0,revenue[1]);
    	chart_magnolia.getData().setItem(1,0,profit[1]);
    	chart_magnolia.getRealTime().endAddData(false,false);
        });
//    $.getJSON('TrackerServlet?action=vendor&vendor=Better%20Brews', function(data) {
//    	chart_popcorn.setDataSource(data);
//        });
//    $.getJSON('TrackerServlet?action=vendor&vendor=Worldly%20Wines', function(data) {
//    	chart_magnolia.setDataSource(data);
//        });
}


setInterval(function(){new_data()}, 2000);


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

<div id="vendor1"></div>
<p>
<div id="Popcorn" style="width:900px;height:300px;display:inline-block"></div>
</p>

<div id="vendor2"></div>
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
