package com.example.isemquestionnaire.controller;

import com.example.isemquestionnaire.exception.NoSuchCategory;
import com.example.isemquestionnaire.model.Questionnaire;
import com.example.isemquestionnaire.repository.QuestionRepository;
import com.example.isemquestionnaire.service.FieldLevelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @GetMapping("download")
    public @ResponseBody ResponseEntity<FileSystemResource> getQuestionnaire() throws IOException {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/questionnaire.xlsx")) {

            Workbook excel = new XSSFWorkbook(inputStream);
            Sheet sheet = excel.getSheet("Лист1");

            Row testRow = sheet.getRow(2);
            Cell testCell = testRow.getCell(0);
            testCell.setCellValue("TEST");

            List<Questionnaire> questionnaires = questionRepository.findAll();
            for (int i = 0; i < questionnaires.size(); i++) {
                Row row = sheet.getRow(i + 2);
                row.getCell(0).setCellValue(i);

                int iterator = 1;
                Field[] fields = Questionnaire.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);

                    if (field.getName().equals("date") || field.getName().equals("id")) continue;

                    String value = String.valueOf(field.get(questionnaires.get(i)));
                    row.createCell(iterator, CellType.STRING).setCellValue(value);

                    iterator++;
                }

            }

            FileOutputStream outputStream = new FileOutputStream("src/main/resources/questionnaire.xlsx");

            excel.write(outputStream);
            excel.close();
        } catch (IOException exception) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Path file = Path.of("src/main/resources/questionnaire.xlsx");

        String headerValue = "attachment; filename=\"%s\"";

        String[] contentType = Files.probeContentType(file).split("/");
        MediaType mediaType = new MediaType(contentType[0], contentType[1]);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, String.format(headerValue, "questionnaire.xlsx"));
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.toFile().length()));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, String.valueOf(mediaType));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new FileSystemResource(file));
    }

}
