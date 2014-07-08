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
	get_product();
}

function loadChart(){	
	chart_products = new cfx.Chart();
	chart_products.getAnimations().getLoad().setEnabled(true);
	chart_products.setGallery(cfx.Gallery.Bar);  
	
	var axisY2 = chart_products.getAxisY2();
	var order_series = chart_products.getSeries().getItem(0);
	order_series.setAxisY(axisY2);	
	
	var divHolder = document.getElementById('Chart');
	chart_products.create(divHolder);
}


function new_data(){
    $.getJSON('TrackerServlet?action=productsales&vendor=<%=request.getParameter("vendor")%>&product_id=<%=request.getParameter("product_id")%>', function(data) {
    	chart_products.setDataSource(data);
        });
}

function get_product(){
    $.getJSON('TrackerServlet?action=product&product_id=<%=request.getParameter("product_id")%>', function(data) {
        $.each(data, function(i, item) {
            $('#results').append($('<tr><td>Product ID</td><td>'+item.product_id+'</td></tr>'));
            $('#results').append($('<input type="hidden" name="product_id" value="'+item.product_id+'">'));
            $('#results').append($('<tr><td>Product Name</td><td>'+item.product_name+'</td></tr>'));
            $('#results').append($('<tr><td>Recommendation</td><td>'+item.recommendation+'</td></tr>'));
            $('#results').append($('<tr><td>Vendor</td><td>'+item.vendor+'</td></tr>'));
            $('#results').append($('<tr><td>Retail Price</td><td><input type="text" name="retail_price" value="'+item.retail_price+'"></td></tr>'));
            $('#results').append($('<tr><td>Sale Price</td><td><input type="text" name="sale_price" value="'+item.sale_price+'"></td></tr>'));
            $('#results').append($('<tr><td>Cost</td><td><input type="text" name="cost" value="'+item.cost+'"></td></tr>'));
            $('#results').append($('<tr><td>Popularity</td><td><input type="text" name="popularity" value="'+item.popularity+'"></td></tr>'));
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
<h1>Product Tracker</h1>
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
<h3><strong>Product <%=request.getParameter("product_id")%></strong></h3>

<div class="contentarea">
<!-- Normal content area start -->
<p>
<div id="Chart" style="width:900px;height:500px;display:inline-block"></div>
</p>

<h3><strong><%=request.getParameter("product_id")%> Details</strong></h3>


<p>    		
<form action="TrackerServlet" method="post">
<table id="results" style="width:400px">
<tr>
<th>Column</th>
<th>Value</th>
</tr>
</table> 
<input type="submit" value="Update Product/Promotion" class="submit" /> 
</form> 
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
