package com.cozycats.ShoppingCart;

import Exceptions.CustomerNotFoundException;
import com.cozycats.Address.AddressService;
import com.cozycats.Customer.CustomerService;
import com.cozycats.Shipping.ShippingRateService;
import com.cozycats.Utility;
import com.cozycats.cozycatscommon.entity.Address;
import com.cozycats.cozycatscommon.entity.CartItem;
import com.cozycats.cozycatscommon.entity.Customer;
import com.cozycats.cozycatscommon.entity.ShippingRate;
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
    @Autowired
    private ShippingRateService shippingRateService;
    @Autowired
    private AddressService addressService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer authenticatedCustomer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(authenticatedCustomer);

        float estimatedTotal = 0.0F;
        for (CartItem item : cartItems ){
            estimatedTotal += item.getSubtotal();
        }
        Address defaultAddress = addressService.getDefaultAddress(authenticatedCustomer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if(defaultAddress != null){
            shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
        }else{
            usePrimaryAddressAsDefault = true;
            shippingRate = shippingRateService.getShippingRateForCustomer(authenticatedCustomer);
        }

        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported", shippingRate != null);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
