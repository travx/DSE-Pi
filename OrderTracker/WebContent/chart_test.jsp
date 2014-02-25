<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; UTF-8">
    <link rel="stylesheet" type="text/css" href="scripts/jchartfx/styles/chartfx.css" />
    <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.system.js"></script>
    <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.coreVector.js"></script>
     <script type="text/javascript" src="scripts/jchartfx/js/jchartfx.animation.js"></script>  
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>

	
<script type="text/javascript">

var chart_summary;

function loadPage(){
	loadSummary();
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



function new_data(){
    $.getJSON('VendorServlet', function(data) {
    	chart_summary.setDataSource(data);
        });
}


setInterval(function(){new_data()}, 10000);


</script>    
    
<title>Performance Data</title>
</head>

<body onload="loadPage()">
<h1>Performance Summary</h1>

<div id="Summary" style="width:700px;height:600px;display:inline-block"></div>

</body>
</html>


