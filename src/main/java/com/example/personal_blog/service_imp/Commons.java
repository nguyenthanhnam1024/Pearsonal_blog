package com.example.personal_blog.service_imp;

import com.example.personal_blog.exception.MyValidateException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Commons {
    public Map<String, String> handlesBindingResult(BindingResult bindingResult) {
        Map<String, String> mapError = new HashMap<>();
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                mapError.put(((FieldError)  error).getField(), error.getDefaultMessage());
            }
        }
        return mapError;
    }

    public void validateListRoleNameToRoleName(String roleNameForValidate, List<String> listRoleNameOfUser) throws MyValidateException {
        for (String roleName : listRoleNameOfUser) {
            if (roleName.equals(roleNameForValidate)) {
                return;
            }
        }
        throw new MyValidateException("you no have rights");
    }
}
