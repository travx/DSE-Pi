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

var chart1;

function loadPage(){
	loadSummary();
	new_data();
}

function loadSummary(){	
	chart1 = new cfx.Chart();
	chart1.getAnimations().getLoad().setEnabled(true); 
	
	var realTime = chart1.getRealTime();
    realTime.setBufferSize(10);
    realTime.setMode(cfx.RealTimeMode.Loop);

    chart1.setGallery(cfx.Gallery.Lines);	

	
	var divHolder = document.getElementById('Summary');
	chart1.create(divHolder);
}



function new_data(){
        chart1.getRealTime().beginAddData(1, cfx.RealTimeAction.Append);
        chart1.getData().setItem(0, 0, 1.2);
        chart1.getRealTime().endAddData(true, false);
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


