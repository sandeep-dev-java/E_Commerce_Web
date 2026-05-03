package Com.E_Commerce.Project.service;

import Com.E_Commerce.Project.DTO.AddressDto;
import Com.E_Commerce.Project.Exception.ResourceNotFoundException;
import Com.E_Commerce.Project.Repository.AddressRepo;
import Com.E_Commerce.Project.model.Address;
import Com.E_Commerce.Project.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AddressRepo addressRepo;
    @Override
    public AddressDto createAddress(AddressDto addressDto, User user) {
       Address  address= modelMapper.map(addressDto,Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);
        Address savedAddress= addressRepo.save(address);

        return modelMapper.map(savedAddress,AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddresses() {
        List<Address> addresses=addressRepo.findAll();
        List<AddressDto> addressDtos= addresses.stream()
                .map(address ->modelMapper.map(address,AddressDto.class))
                .collect(Collectors.toList());
        return addressDtos ;
    }

    @Override
    public AddressDto getAddressById(Long addressId) {
        Address address= addressRepo.findById(addressId)
                .orElseThrow(()->
                 new ResourceNotFoundException("Adress","addressId",addressId));
        return modelMapper.map(address,AddressDto.classs);
    }
}