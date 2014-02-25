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

var chart_products;

function loadPage(){
	loadChart();
	new_data();
	get_products();
}

function loadChart(){	
	chart_products = new cfx.Chart();
	chart_products.getAnimations().getLoad().setEnabled(true);
	chart_products.setGallery(cfx.Gallery.Bar);  
	
	var fields = chart_products.getDataSourceSettings().getFields();
	var field1 = new cfx.FieldMap();
	var field2 = new cfx.FieldMap();
	var field3 = new cfx.FieldMap();
	
	field1.setName("product_id");
	field1.setDisplayName("Product");
	field1.setUsage(cfx.FieldUsage.Label);
	fields.add(field1);
	
	field2.setName("profit");
	field2.setDisplayName("Profit");
	field2.setUsage(cfx.FieldUsage.Value);
	fields.add(field2);	
	
	var divHolder = document.getElementById('Chart');
	chart_products.create(divHolder);
}


function new_data(){
    $.getJSON('TrackerServlet?action=vendorproducts&vendor=<%=request.getParameter("vendor")%>', function(data) {
    	chart_products.setDataSource(data);
        });
}

function get_products(){
    $.getJSON('TrackerServlet?action=vendorproducts&vendor=<%=request.getParameter("vendor")%>', function(data) {
        $.each(data, function(i, item) {
            $('#results').append($('<tr><td><a href="product.jsp?product_id=' + item.product_id + '">'+item.product_id+'</a></td><td>'+item.profit+'</td></tr>'));
        });
     });
}


setInterval(function(){new_data()}, 10000);


</script>        
    
</head>

<body onload="loadPage()">

<div id="upbg">
</div>

<div id="outer">

<div id="header">

<div id="headercontent">
<h1>Vendor Tracker</h1>
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
<h3><strong><%=request.getParameter("vendor")%></strong></h3>

<div class="contentarea">
<!-- Normal content area start -->
<p>
<div id="Chart" style="width:900px;height:500px;display:inline-block"></div>
</p>


<p>    		
<table id="results">
<tr>
<th>Product ID</th>
<th>Profit</th>
</tr>
</table>  
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
