package com.cricketScore.api.services.Impl;

import com.cricketScore.api.entities.Match;
import com.cricketScore.api.repositories.MatchRepository;
import com.cricketScore.api.services.CricketService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CricketServiceImpl implements CricketService {

    private final MatchRepository matchRepo;

    public CricketServiceImpl(MatchRepository matchRepo) {
        this.matchRepo = matchRepo;
    }

    @Override
    public List<Match> getLiveMatchScores() {
        String url = "https://www.cricbuzz.com/cricket-match/live-scores";
        List<Match> matches = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements liveScoreElements = document.select("div.cb-mtch-lst.cb-tms-itm");

            liveScoreElements.stream()
                    .map(this::parseMatch)
                    .forEach(this::updateMatch);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }

    private Match parseMatch(Element matchElement) {
        String teamsHeading = matchElement.select("h3.cb-lv-scr-mtch-hdr a").text();
        String matchNumberVenue = matchElement.select("span").text();

        String battingTeam = matchElement.select("div.cb-hmscg-bat-txt div.cb-hmscg-tm-nm").text();
        String score = matchElement.select("div.cb-hmscg-bat-txt div.cb-hmscg-tm-nm+div").text();

        String bowlTeam = matchElement.select("div.cb-hmscg-bwl-txt div.cb-hmscg-tm-nm").text();
        String bowlTeamScore = matchElement.select("div.cb-hmscg-bwl-txt div.cb-hmscg-tm-nm+div").text();

        String textLive = matchElement.select("div.cb-text-live").text();
        String textComplete = matchElement.select("div.cb-text-complete").text();
        String matchLink = matchElement.select("a.cb-lv-scrs-well.cb-lv-scrs-well-live").attr("href");

        Match match = new Match();
        match.setTeamHeading(teamsHeading);
        match.setMatchNumberVenue(matchNumberVenue);
        match.setBattingTeam(battingTeam);
        match.setBattingTeamScore(score);
        match.setBowlTeam(bowlTeam);
        match.setBowlTeamScore(bowlTeamScore);
        match.setLiveText(textLive);
        match.setTextComplete(textComplete);
        match.setMatchLink(matchLink);

        return match;
    }

    private void updateMatch(Match match) {
        matchRepo.findByTeamHeading(match.getTeamHeading())
                .ifPresentOrElse(existingMatch -> {
                    match.setMatchId(existingMatch.getMatchId());
                    matchRepo.save(match);
                }, () -> matchRepo.save(match));
    }

    @Override
    public List<Match> getAllMatch() {
        return matchRepo.findAll();
    }

    @Override
    public List<List<String>> getCWC2024PointTable() {
        String tableURL = "https://www.cricbuzz.com/cricket-series/6732/icc-cricket-world-cup-2023/points-table";
        List<List<String>> pointTable = new ArrayList<>();

        try {
            Document document = Jsoup.connect(tableURL).get();
            Elements table = document.select("table.cb-srs-pnts");

            List<String> headers = table.select("thead tr th")
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toList());
            pointTable.add(headers);

            table.select("tbody tr").stream()
                    .filter(tr -> tr.hasAttr("class"))
                    .forEach(tr -> {
                        List<String> points = new ArrayList<>();
                        points.add(tr.select("div.cb-col-84").text());
                        tr.select("td").stream()
                                .filter(td -> !td.hasClass("cb-srs-pnts-name"))
                                .map(Element::text)
                                .forEach(points::add);
                        pointTable.add(points);
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pointTable;
    }
}
