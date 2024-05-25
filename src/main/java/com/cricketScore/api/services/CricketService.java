package com.cricketScore.api.services;

import com.cricketScore.api.entities.Match;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CricketService {


    List<Match> getLiveMatchScores();
    List<Match> getAllMatch();
    List<List<String>> getCWC2024PointTable();


}
