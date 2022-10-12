package com.cozycats.ShppingCart;

import Exceptions.CustomerNotFoundException;
import com.cozycats.Customer.CustomerService;
import com.cozycats.Utility;
import com.cozycats.cozycatscommon.entity.CartItem;
import com.cozycats.cozycatscommon.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer authenticatedCustomer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(authenticatedCustomer);

        float estimatedTotal = 0.0F;
        for (CartItem item : cartItems ){
            estimatedTotal += item.getSubtotal();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
