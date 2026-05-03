package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    CartDTO addProductCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, int quantity);

    String deleteProdcutFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
