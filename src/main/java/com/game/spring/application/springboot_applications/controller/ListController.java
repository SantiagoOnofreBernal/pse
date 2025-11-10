package com.game.spring.application.springboot_applications.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@Controller
public class ListController {


    private static final Map<Integer, List<Question>> SURVEYS;
    static {
        Map<Integer, List<Question>> m = new HashMap<>();
        m.put(1, Arrays.asList(
                new Question("Nombre",  "visible", "hidden", null),
                new Question("Apellido","visible", "hidden", null),
                new Question("Cargo",   "visible", "hidden", null)
        ));
        m.put(2, Arrays.asList(
                new Question("Pregunta 1","visible", "hidden", null),
                new Question("Pregunta 2","visible", "hidden", null),
                new Question("Pregunta 3","visible", "hidden", null)
        ));
        m.put(3, Arrays.asList(
                new Question("Pregunta 4","hidden", "visible",
                        Arrays.asList("Opción A", "Opción B", "Opción C"))
        ));
        SURVEYS = Collections.unmodifiableMap(m);
    }

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/encuesta/1";
    }

    @GetMapping({"/encuesta", "/encuesta/", "/encuesta/{id}"})
    public String home(@PathVariable(required = false) Integer id, Model model) {
        Integer surveyId = Optional.ofNullable(id).orElse(1); 
        model.addAttribute("surveyId", surveyId);
        model.addAttribute("title", "Encuesta " + surveyId);

  
        List<Question> questions = SURVEYS.getOrDefault(surveyId, SURVEYS.get(1));

        model.addAttribute("questions", questions);
        return "list";
    }

    
    public static class Question {
        private final String label;
        private final String inputClass;
        private final String selectClass;
        private final List<String> options;

        public Question(String label, String inputClass, String selectClass, List<String> options) {
            this.label = label;
            this.inputClass = inputClass;
            this.selectClass = selectClass;
            this.options = options;
        }

        public String getLabel() { return label; }
        public String getInputClass() { return inputClass; }
        public String getSelectClass() { return selectClass; }
        public List<String> getOptions() { return options; }
    }
}