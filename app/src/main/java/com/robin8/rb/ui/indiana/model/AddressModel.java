package com.robin8.rb.ui.indiana.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;

/**
 * Created by IBM on 2016/7/25.
 */
public class AddressModel extends BaseBean implements Serializable {

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private Address address;

    public class Address{
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        private String name;
        private String phone;
        private String region;
        private String location;
    }
}
