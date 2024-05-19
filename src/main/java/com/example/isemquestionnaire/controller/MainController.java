package com.example.isemquestionnaire.controller;

import com.example.isemquestionnaire.exception.NoSuchCategory;
import com.example.isemquestionnaire.model.Questionnaire;
import com.example.isemquestionnaire.repository.QuestionRepository;
import com.example.isemquestionnaire.service.FieldLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    private final FieldLevelService fieldLevelService;
    private final QuestionRepository questionRepository;

    @Autowired
    public MainController(FieldLevelService fieldLevelService, QuestionRepository questionRepository) {
        this.fieldLevelService = fieldLevelService;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("questionnaire", new Questionnaire());
        return "index";
    }

    @PostMapping
    public String process(@ModelAttribute Questionnaire questionnaire) {
        questionRepository.save(questionnaire);
        log.info(questionnaire.toString());
        return "redirect:/";
    }

    @GetMapping("get_knowledge_section")
    public @ResponseBody ResponseEntity<List<String>> getKnowledgeLevel(@RequestParam(value = "classifier") String knowledgeClassifier,
                                                                        @RequestParam(value = "level") String sectionLevel) {
        try {
            List<String> resultList;

            String knowledgeLevel = sectionLevel.split("_")[0];
            String keyword = sectionLevel.split("_")[1];

            switch (knowledgeClassifier) {
                case "first" -> {
                    if (knowledgeLevel.equals("second")) resultList = fieldLevelService.getFCSecondLevel(keyword);
                    else resultList = fieldLevelService.getFCThirdLevel(keyword);

                    return ResponseEntity.ok(resultList);
                }
                case "second" -> {
                    if (knowledgeLevel.equals("second")) resultList = fieldLevelService.getSCSecondLevel(keyword);
                    else resultList = fieldLevelService.getSCThirdLevel(keyword);

                    return ResponseEntity.ok(resultList);
                }
                default -> throw new NoSuchCategory("Нет такого классификатора");
            }
        } catch (NoSuchCategory exception) {
            log.info(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
