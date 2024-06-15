package com.CSDLPT.ManagingMaterials.EN_Employee;

import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ReqDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.EN_Employee.dtos.ResDtoReportForEmployeeActivities;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ReqDtoRetrievingData;
import com.CSDLPT.ManagingMaterials.Module_FindingAction.dtos.ResDtoRetrievingData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Validator;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService.BranchServices branchServices;
    private final Validator hibernateValidator;
    private final Logger logger;

    /** Spring MVC: Branch-role controllers **/
    /*_____________RequestMethod.GET: Header-pages_____________*/
    @GetMapping("/branch/employee/manage-employee")
    public ModelAndView getManageEmployeePage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getManageEmployeePage(request, model);
    }
    @GetMapping("/branch/employee/report-for-employee")
    public ModelAndView getReportForEmployeePage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getReportForEmployeePage(request, model);
    }
    @GetMapping("/branch/employee/report-for-employee-activities")
    public ModelAndView getReportForEmployeeActivitiesPage(HttpServletRequest request, Model model) throws SQLException {
        return branchServices.getReportForEmployeeActivitiesPage(request, model);
    }

    /*_____________RequestMethod.POST: Employee-entity-interaction_____________*/
    @PostMapping("${url.post.branch.prefix.v1}/find-employee-by-values")
    public ResponseEntity<ResDtoRetrievingData<Employee>> findingEmployeesByValues(
        @RequestBody ReqDtoRetrievingData<Employee> searchingObject,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findEmployee(request, searchingObject));
        } catch (Exception e) {
            logger.info(e.toString());
            return null;
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/find-all-employee-activities-for-report")
    public ResponseEntity<ResDtoRetrievingData<ResDtoReportForEmployeeActivities>> findAllEmployeeActivities(
        @RequestBody ReqDtoReportForEmployeeActivities requiredInfoToSearchEmpActivities,
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(branchServices.findAllEmployeeActivities(request, requiredInfoToSearchEmpActivities));
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("${url.post.branch.prefix.v1}/add-employee")
    public String addEmployee(
        @ModelAttribute("employee") Employee employee,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        Set<ConstraintViolation<Employee>> violations = hibernateValidator.validate(employee);
        if (!violations.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCode", violations.iterator().next().getMessage());
            redirectAttributes.addFlashAttribute("submittedEmployee", employee);
            return "redirect:" + standingUrl;
        }

        try {
            branchServices.addEmployee(request, employee);
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

    @PostMapping("${url.post.branch.prefix.v1}/update-employee")
    public String updateEmployee(
        @ModelAttribute("employee") Employee employee,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            branchServices.updateEmployee(employee, request);
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

    @PostMapping("${url.post.branch.prefix.v1}/delete-employee")
    public String deleteEmployee(
        @RequestParam("deleteBtn") String employeeId,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        final String standingUrl = request.getHeader("Referer");
        try {
            branchServices.deleteEmployee(employeeId, request);
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