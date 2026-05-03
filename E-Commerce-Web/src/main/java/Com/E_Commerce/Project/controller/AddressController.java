package Com.E_Commerce.Project.controller;

import Com.E_Commerce.Project.DTO.AddressDto;
import Com.E_Commerce.Project.model.User;
import Com.E_Commerce.Project.service.AddressService;
import Com.E_Commerce.Project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/address")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto){
        User user= authUtil.loggedInUser();
        AddressDto savedAddressDto=addressService.createAddress(addressDto, user);
     return  new ResponseEntity<>(savedAddressDto, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public  ResponseEntity<List<AddressDto>> getAddress(){
        List<AddressDto> addressList= addressService.getAddresses();
        return  new ResponseEntity<>(addressList,HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public  ResponseEntity<AddressDto> getAddressByID(@PathVariable Long addressId){
        AddressDto addressDto= addressService.getAddressById(addressId);
        return  new ResponseEntity<>(addressDto,HttpStatus.OK);
    }



}
