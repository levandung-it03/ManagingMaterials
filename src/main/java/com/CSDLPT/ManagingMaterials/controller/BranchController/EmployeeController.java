package com.CSDLPT.ManagingMaterials.controller.BranchController;

import com.CSDLPT.ManagingMaterials.dto.ReqDtoFindingAction;
import com.CSDLPT.ManagingMaterials.model.Employee;
import com.CSDLPT.ManagingMaterials.service.BranchService.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("${url.post.branch.prefix.v1}")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final Validator hibernateValidator;
    private final Logger logger;
    
    @PostMapping("/find-employee-by-values")
    public ResponseEntity<List<Employee>> findingEmployeesByValues(
        @RequestBody ReqDtoFindingAction<Employee> searchingObject,
        HttpServletRequest request
    ){
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.findEmployee(request, searchingObject));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(List.of());
        }
    }
    
    @PostMapping("/add-employee")
    public String addEmployee(
        @ModelAttribute("employee") Employee employee,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<Employee>> violations = hibernateValidator.validate(employee);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
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

    @PostMapping("/update-employee")
    public String updateEmployee(
        @ModelAttribute("employee") Employee employee,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            employeeService.updateEmployee(employee, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_update_01");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
            logger.info(e.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.toString());
        }
        return "redirect:" + standingUrl;
    }

    @PostMapping("/delete-employee")
    public String deleteEmployee(
        @RequestParam("deleteBtn") String employeeId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            employeeService.deleteEmployee(employeeId, request);
            redirectAttributes.addFlashAttribute("succeedCode", "succeed_delete_01");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_entity_01");
            logger.info(e.toString());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorCode", "error_systemApplication_01");
            logger.info(e.toString());
        }
        return "redirect:" + standingUrl;
    }
}