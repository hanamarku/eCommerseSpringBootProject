package com.cozycats.cozycatsbackend.admin.Order;

import com.cozycats.cozycatsbackend.admin.Product.ProductService;
import com.cozycats.cozycatsbackend.admin.paging.PagingAndSortingHelper;
import com.cozycats.cozycatsbackend.admin.paging.PagingAndSortingParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ProductSearchController {

	@Autowired
	private ProductService service;
	
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";
	}
	
	@PostMapping("/orders/search_product")
	public String searchProducts(String keyword) {
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	
	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
									   @Param("sortField") String sortField, @Param("sortDir") String sortDir,
									   @Param("keyword") String keyword) {
		service.searchProducts(pageNum, sortField, sortDir, keyword);
		return "orders/search_product";
	}
}
