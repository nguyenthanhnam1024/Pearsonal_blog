//package com.example.personal_blog.service_imp;
//
//import com.example.personal_blog.dto.EmailPatternDTO;
//import com.example.personal_blog.entity.ContentEmailPattern;
//import com.example.personal_blog.entity.EmailPattern;
//import com.example.personal_blog.exception.MyValidateException;
//import com.example.personal_blog.repository.ContentEmailPatternRepo;
//import com.example.personal_blog.repository.EmailPatternRepo;
//import com.example.personal_blog.service.EmailPatternService;
//import lombok.AllArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.BindingResult;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class EmailPatternServiceImp implements EmailPatternService {
//    private final Commons commons;
//    private final EmailPatternRepo emailPatternRepo;
//    private final ContentEmailPatternRepo contentEmailPatternRepo;
//
//    @Override
//    public void validateRoleAndRequestData(HttpServletRequest request, BindingResult result) throws MyValidateException {
//        commons.validateForADMIN(request);
//        if (result.hasErrors()) {
//            throw new MyValidateException("request invalid");
//        }
//    }
//
//    @Override
//    @Transactional
//    public Object saveEmailPattern(HttpServletRequest request, EmailPatternDTO emailPatternDTO, BindingResult result) throws MyValidateException {
//        this.validateRoleAndRequestData(request, result);
//        ModelMapper modelMapper = new ModelMapper();
//        EmailPattern emailPattern = modelMapper.map(emailPatternDTO, EmailPattern.class);
//        try {
//            EmailPattern emailPatternStored = emailPatternRepo.save(emailPattern);
//            int newId = 0;
//            List<ContentEmailPattern> contents = emailPatternDTO.getContents();
//            for (ContentEmailPattern content : contents) {
//                content.setEmailPatternId(emailPatternStored.getEmailPatternId());
//                content.setContentEmailPatternId(newId++);
//                contentEmailPatternRepo.save(content);
//            }
//            return emailPatternStored;
//        } catch (Exception e) {
//            throw new MyValidateException("Error saving email pattern");
//        }
//    }
//}
