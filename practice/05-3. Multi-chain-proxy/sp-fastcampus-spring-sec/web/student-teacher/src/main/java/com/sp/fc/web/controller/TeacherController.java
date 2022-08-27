package com.sp.fc.web.controller;

import com.sp.fc.web.student.StudentAuthenticationProvider;
import com.sp.fc.web.teacher.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final StudentAuthenticationProvider studentAuthenticationProvider;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model)
    {
        model.addAttribute("studentList", studentAuthenticationProvider.getStudents(teacher.getId()));
        return "TeacherMain";
    }


}
