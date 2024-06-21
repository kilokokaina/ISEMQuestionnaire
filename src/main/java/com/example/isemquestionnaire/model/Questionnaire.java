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

    private String name;
    private String email;

    private Byte age;

    private String FCFirstLevel1;
    private String FCSecondLevel1;
    private String FCThirdLevel1;
    private String FCKnowledgeLevel1;

    private String FCFirstLevel2;
    private String FCSecondLevel2;
    private String FCThirdLevel2;
    private String FCKnowledgeLevel2;

    private String expertFIO;
    private String additionSources;

    private int messageFrequency;
    private int informationTransfer;
    private int chiefAvailable;
    private int innerCommunication;
    private int projectInvolve;
    private int effortResult;
    private int instituteSupport;
    private int teamRespect;
    private String skillDevelop;
    private int developOpportunity;

    private int knowledgeFixation;
    private int knowledgeShare;
    private int knowledgeFindingInner;
    private int knowledgeFindingOuter;

}
