package Com.E_Commerce.Project.controller;

import Com.E_Commerce.Project.DTO.CartDTO;
import Com.E_Commerce.Project.Repository.CartRepo;
import Com.E_Commerce.Project.model.Cart;
import Com.E_Commerce.Project.service.CartService;
import Com.E_Commerce.Project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProduct(@PathVariable Long productId,
                                                @PathVariable   Integer quantity){
        CartDTO cartDTO= cartService.addProductCart(productId,quantity);
        return  new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS= cartService.getAllCarts();
        return  new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/carts/user/cart")
    public ResponseEntity<CartDTO> getCartById(){
        String emailId= authUtil.loggedInEmail();
      Cart cart = cartRepo.findCartByEmail(emailId);
      Long cartId =  cart.getCartId();
        CartDTO cartDTO= cartService.getCart(emailId,cartId);
        return  new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO=  cartService.updateProductQuantityInCart(productId,
                     operation.equalsIgnoreCase("delete") ? -1: 1);


    return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);

    }

    @DeleteMapping("carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(Long cartId,Long productId){
      String status=  cartService.deleteProdcutFromCart(cartId,productId);
    return  new ResponseEntity<String>(status,HttpStatus.OK);
    }

}
