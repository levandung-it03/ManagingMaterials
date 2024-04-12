package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final Validator hibernateValidator;

    @PostMapping("/add-employee")
    @ModelAttribute("employee")
    public String addEmployee(Employee employee, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        final String standingUrl = request.getHeader("Referer");
        Errors validationErr = hibernateValidator.validateObject(employee);
        if (validationErr.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorCode", validationErr.getFieldErrors().getFirst().
                getDefaultMessage());
            return "redirect:" + standingUrl;
        }

        try {
            employeeService.addEmployee(request, employee);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_add_01");
        } catch (DuplicateKeyException ignored) {
            redirectAttributes.addFlashAttribute("submittedEmployee", employee);
            redirectAttributes.addFlashAttribute("errorCode", "error_employee_01");
        } catch (Exception ignored) {
            redirectAttributes.addFlashAttribute("submittedEmployee", employee);
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
        }
        return "redirect:" + standingUrl;
    }

}
