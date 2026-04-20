package Com.E_Commerce.Project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Table(name="Address")
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AddressId;
    @NotBlank
    @Size(min=5,message = "Street name must be atleast 5 Characters")
    private String Street;

    public Address(String street, String city, String country, String state, String pincode) {
        Street = street;
        City = city;
        this.country = country;
        this.state = state;
        this.pincode = pincode;
    }

    @NotBlank
    @Size(min=4,message = "city name must be atleast 4 Characters")
    private  String City;
    @NotBlank
    @Size(min=5,message = "country name must be atleast 5 Characters")
    private String country;
    @NotBlank
    @Size(min=3,message = "State name must be atleast 3 Characters")
    private String state;
    @NotBlank
    @Size(min=6,message = "code name must be atleast 6 Characters")
    private String pincode;
     @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users= new ArrayList<>();




}
