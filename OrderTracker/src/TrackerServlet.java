

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TrackerServlet
 */
public class TrackerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Database db;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrackerServlet() {
        super();
        // TODO Auto-generated constructor stub
		String nodes[] = {"ras1", "ras2", "ras3"};
		db = new Database(nodes, "simulation");
		db.connect();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if (action.equals("summary")){
			doSummary(request, response); 
		}
		else if (action.equals("vendorsummary")){
			doVendorSummary(request, response); 
		}
		else if (action.equals("vendor")){
			doVendor(request, response); 
		}
		else if (action.equals("vendorproducts")){
			doVendorProducts(request, response); 
		}
		else if (action.equals("productsales")){
			doProductSales(request, response); 
		}
		else if (action.equals("product")){
			doProduct(request, response); 
		}		
		else {
			response.getWriter().println(action);
			response.getWriter().println("Error processing servlet request.");
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		db.updateProductPrice(Double.parseDouble(request.getParameter("retail_price")), Double.parseDouble(request.getParameter("sale_price")), Double.parseDouble(request.getParameter("cost")), Double.parseDouble(request.getParameter("popularity")), request.getParameter("product_id"));
		request.setAttribute("message", "Product Information Saved");
		request.getRequestDispatcher("product.jsp").forward(request, response);
	}

	public void doSummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().write(db.getSummary());
	}
	
	public void doVendorSummary(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(db.getVendorSummary());
	}

	public void doVendor(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(db.getVendorDetails(request.getParameter("vendor")));
	}
	
	public void doVendorProducts(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(db.getProductSummary(request.getParameter("vendor")));
	}
	
	public void doProductSales(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(db.getProductDetail(request.getParameter("product_id")));
	}	
	
	public void doProduct(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write(db.getProductInfo(request.getParameter("product_id")));
	}		
}
