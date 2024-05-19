package com.example.isemquestionnaire.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Questionnaire {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date = new Date();

    private String firstName;
    private String secondName;
    private String email;

    private String FCFirstLevel1;
    private String FCSecondLevel1;
    private String FCThirdLevel1;
    private String FCKnowledgeLevel1;
    private String FCExpertFIO1;
    private String FCAdditionSources1;

    private String FCFirstLevel2;
    private String FCSecondLevel2;
    private String FCThirdLevel2;
    private String FCKnowledgeLevel2;
    private String FCExpertFIO2;
    private String FCAdditionSources2;

    private String SCFirstLevel1;
    private String SCSecondLevel1;
    private String SCThirdLevel1;
    private String SCKnowledgeLevel1;
    private String SCExpertFIO1;
    private String SCAdditionSources1;

    private String SCFirstLevel2;
    private String SCSecondLevel2;
    private String SCThirdLevel2;
    private String SCKnowledgeLevel2;
    private String SCExpertFIO2;
    private String SCAdditionSources2;

    private int knowledgeFixation;
    private int knowledgeShare;
    private int knowledgeFindingInner;
    private int knowledgeFindingOuter;

    private String interactEmployee;
    private String interactFrequency;

}
