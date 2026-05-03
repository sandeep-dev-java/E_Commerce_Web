package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.CartDTO;
import Com.E_Commerce.Project.DTO.ProductDto;
import Com.E_Commerce.Project.Exception.APIException;
import Com.E_Commerce.Project.Exception.ResourceNotFoundException;
import Com.E_Commerce.Project.Repository.CartItemRepo;
import Com.E_Commerce.Project.Repository.CartRepo;
import Com.E_Commerce.Project.Repository.ProductRepository;
import Com.E_Commerce.Project.model.Cart;
import Com.E_Commerce.Project.model.CartItem;
import Com.E_Commerce.Project.model.Product;
import Com.E_Commerce.Project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ProductRepository productRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CartItemRepo cartItemRepo;
    @Override
    public CartDTO addProductCart(Long productId, Integer quantity) {
         //First find existing or create one
         Cart cart= createCart();
        //retrive the product Deatils;
        Product product=
                productRepository.
                        findById(productId).
                        orElseThrow(()->
                                new ResourceNotFoundException
                                        ("product","productId",productId));
        //Performe Validation
        //product exist or not validation
       CartItem cartItem= cartItemRepo.findCartItemByProductIdAndCartId(
                cart.getCartId(),productId
        );
        if(cartItem!=null){
            throw  new APIException("Product "+product.getProductName()+"is Not Available");
        }
        if(product.getQuantity()==0){
            throw  new APIException(product.getProductName()+"is not avaialble");
        }
            //quantity have exist or not
        if(product.getQuantity()<quantity){
            throw  new APIException("please make an order of the "
                    +product.getProductName()+
                    " less than or equal to the quantity "+
                    product.getQuantity());
        }
        //creat Cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());


        //save cart item
        cartItemRepo.save(newCartItem);
        // 👉 ADD THIS LINE
        cart.getCartItems().add(newCartItem);
        product.setQuantity(product.getQuantity());
     cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
        //Return updated cart
      cartRepo.save(cart);
      CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);
       List<CartItem> cartItems1=cart.getCartItems();
        Stream<ProductDto> productDtoStream= cartItems1.stream()
                .map((item)-> {
                    ProductDto map= modelMapper.map(item.getProduct(),ProductDto.class);
                    map.setQuantity(item.getQuantity());
                     return map;
                });

        cartDTO.setProductDtos(productDtoStream.toList());
        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts= cartRepo.findAll();
        if(carts.size()==0){
            throw  new APIException("No cart Exist");
        }else{
            List<CartDTO> cartDTOS= carts.stream().
                    map(cart -> {
                CartDTO cartDTO =
                        modelMapper.map(cart, CartDTO.class);
                List<ProductDto> products = cart.getCartItems().stream()
                        .map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(toList());
                cartDTO.setProductDtos(products);
                return  cartDTO;
            }).collect(toList());
            return cartDTOS ;
        }


    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
     Cart cart = cartRepo.findEmailAndCartId(emailId,cartId);
     if(cart==null){
         throw new ResourceNotFoundException("cart","cartId",cartId);

     }
     CartDTO cartDTO= modelMapper.map(cart,CartDTO.class);
        List<ProductDto> productDtos=  cart.getCartItems().stream()
                .map(p-> modelMapper.map(p.getProduct(),ProductDto.class))
        .toList();
        cartDTO.setProductDtos(productDtos);
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, int quantity) {
        String mail = authUtil.loggedInEmail();
        Cart userCart= cartRepo.findCartByEmail(mail);
        Long cartId= userCart.getCartId();
        Cart cart= cartRepo.findById(cartId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "cart","cartId",cartId));
        Product product= productRepository.findById(productId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException
                                ("product","prodctId",productId));

        if(product.getQuantity()==0){
            throw  new APIException(product.getProductName()+"is not avaialble");
        }
        //quantity have exist or not
        if(product.getQuantity()<quantity){
            throw  new APIException("please make an order of the "
                    +product.getProductName()+
                    " less than or equal to the quantity "+
                    product.getQuantity());
        }

        //validatio nwhen pass find and adding cartitem
         CartItem cartItem= cartItemRepo.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem==null){
            throw  new APIException("product "+ product.getProductName()+"product not avavilable");
        }


        int  newQuantity = cartItem.getQuantity()+quantity;

        if(newQuantity<0){
            throw new APIException("the resulting quantity cannot be nagative");
        }
        if(newQuantity==0){
            deleteProdcutFromCart(cartId,productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);//current quantity + qantity(update wala)
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepo.save(cart);
        }
        CartItem updatedItem= cartItemRepo.save(cartItem);
        if(updatedItem.getQuantity()==0){
            cartItemRepo.deleteById(cartItem.getCartItemId());

        }
        CartDTO cartDto= modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems= cart.getCartItems();
        Stream<ProductDto> productDtoStream=
                cartItems.stream().map((item)->{
                    ProductDto prd= modelMapper.map(item.getProduct(),ProductDto.class);
              prd.setQuantity(item.getQuantity());
                return prd;
                });
        cartDto.setProductDtos(productDtoStream.toList());
        return cartDto ;
    }

    @Transactional
    @Override
    public String deleteProdcutFromCart(Long cartId, Long productId) {
       Cart cart = cartRepo.findById(cartId).orElseThrow(
               ()->  new ResourceNotFoundException("cart","cartId",cartId));
       CartItem cartItem= cartItemRepo.findCartItemByProductIdAndCartId(cartId,productId);


        if (cartItem == null) {
            throw new ResourceNotFoundException("product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItemRepo.deleteCartItemByProductIdAndCartId(cartId,productId);
            return "product "+cartItem.getProduct().getProductName()+"removed from cart ";
        }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepo.save(cartItem);

    }

    private  Cart createCart(){
        Cart userCart= cartRepo.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null){
            return userCart;
        }//if not then create
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepo.save(cart);

        return newCart;
    }
    }


