package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Address;
import dev.jnx.jjshop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired private AddressService addressService;

    @RequestMapping("/add")
    RespBean add(String addressDetail, String areaCode, String province, String city, String county,
                 boolean isDefault, @RequestParam("tel") String mobile, String name, Integer currentUid,
                 Integer id, String mode){
        Address addr = new Address();
        addr.setAddressDetail(addressDetail);
        addr.setAreaCode(areaCode);
        addr.setCity(city);
        addr.setProvince(province);
        addr.setCounty(county);
        addr.setDefault(isDefault);
        addr.setMobile(mobile);
        addr.setName(name);
        addr.setUid(currentUid);
        if (id != null) addr.setId(id);
        if (mode.equals("add")){
            addressService.add(addr);
            if (isDefault){
                addressService.setDefault(addr.getId(), currentUid);
            }
            return RespBean.ok("添加成功");
        }else{
            int ret = addressService.update(addr);
            if (isDefault){
                addressService.setDefault(addr.getId(), currentUid);
            }
            return ret == 0 ? RespBean.error("修改失败") : RespBean.ok("修改成功");
        }
    }

    @RequestMapping("/list")
    RespBean list(int currentUid){
        List<Address> addrList = addressService.list(currentUid);
        return RespBean.ok("获取成功", addrList);
    }

    @RequestMapping("/delete")
    RespBean delete(int id, int currentUid){
        Address addr = addressService.get(id);
        if (addr == null){
            return RespBean.error("id不存在");
        }

        int ret = addressService.delete(id, currentUid);
        if (ret != 1){
            return RespBean.error("删除失败");
        }
        if (addr.isDefault()){
            List<Address> list = addressService.list(addr.getUid());
            addressService.setDefault(list.get(0).getId(), addr.getUid());
        }
        return RespBean.ok("删除成功");
    }


}
