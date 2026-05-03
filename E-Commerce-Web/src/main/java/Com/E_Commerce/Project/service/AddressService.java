package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.AddressDto;
import Com.E_Commerce.Project.model.User;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(AddressDto addressDto, User user);

    List<AddressDto> getAddresses();

    AddressDto getAddressById(Long addressId);
}
