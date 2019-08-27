package dev.jnx.jjshop.service;

import dev.jnx.jjshop.dao.AddressDao;
import dev.jnx.jjshop.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService{
    @Autowired private AddressDao addressDao;

    public Address getDefault(int uid) {
        return addressDao.getDefault(uid);
    }

    public void setDefault(int id, int uid) {
        int ret = addressDao.setDefault(id, uid);
    }

    public List<Address> list(int uid) {
        return addressDao.list(uid);
    }

    public int add(Address addr) {
        return addressDao.add(addr);
    }

    public int delete(int id, int uid) {
        return addressDao.delete(id, uid);
    }

    public int update(Address addr) {
        return addressDao.update(addr);
    }

    public Address get(int id){
        return addressDao.get(id);
    }
}
