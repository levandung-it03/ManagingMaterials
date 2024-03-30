package com.CSDLPT.ManagingMaterials.config;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class StaticUtilMethods {
    private final Map<String, String> responseMessages;

    /**Spring MVC: Customize returned ModelAndView to show ErrMessage or SucceedMessages.**/
    public ModelAndView customResponseModelView(@NonNull Map<String, Object> model, String pageModel) {
        ModelAndView modelAndView = new ModelAndView(pageModel);

        Object errCode = model.get("errorCode");
        if (errCode != null)
            modelAndView.addObject("errorMessage", responseMessages.get(errCode.toString()));

        Object succeedCode = model.get("succeedCode");
        if (succeedCode != null)
            modelAndView.addObject("succeedMessage", responseMessages.get(succeedCode.toString()));

        return modelAndView;
    }
}
