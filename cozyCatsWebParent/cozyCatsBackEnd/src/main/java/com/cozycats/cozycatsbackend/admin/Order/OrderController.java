package com.cozycats.cozycatsbackend.admin.Order;

import Exceptions.OrderNotFoundException;
import com.cozycats.cozycatsbackend.admin.Brand.BrandService;
import com.cozycats.cozycatsbackend.admin.Setting.SettingService;
import com.cozycats.cozycatsbackend.admin.paging.PagingAndSortingHelper;
import com.cozycats.cozycatsbackend.admin.paging.PagingAndSortingParam;
import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Order;
import com.cozycats.cozycatscommon.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {
    private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired
    private OrderService orderService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectURL;
    }

    @GetMapping("/orders/page/{pageNum}")

    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("sortField") String sortField, @Param("sortDir") String sortDir,
            @Param("keyword") String keyword, HttpServletRequest request
    ) {
        Page<Order> page = orderService.listByPage(pageNum, sortField, sortDir, keyword);
        loadCurrencySetting(request);
        List<Order> listOrders = page.getContent();

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listOrders", listOrders);

        return "orders/orders";
    }
//    public String listByPage(
//            @PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
//            @PathVariable(name = "pageNum") int pageNum,
//            HttpServletRequest request) {
//
//        orderService.listByPage(pageNum, helper);
//        loadCurrencySetting(request);
//
//        return "orders/orders";
//    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes ra, HttpServletRequest request) {
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);
            model.addAttribute("order", order);

            return "orders/order_details_modal";
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra){
        try {
            orderService.delete(id);
            ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
        } catch (OrderNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return defaultRedirectURL;
    }

}
