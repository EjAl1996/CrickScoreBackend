**Cricket Score API Service**

This project is a Cricket Score Scraping Service that fetches live cricket match scores and point tables from a cricket website (Cricbuzz) and stores the relevant data in a database. It utilizes Spring Boot for the backend service and Jsoup for HTML parsing. The API exposes endpoints to fetch live match scores, update match details in the database, and retrieve the Cricket World Cup 2024 points table.

**Features**

Fetch Live Match Scores: Scrapes live match data such as team names, scores, and match status from Cricbuzz using Jsoup and stores it in the database.
Database Integration: Matches are saved in the database using Spring Data JPA and automatically updated if the match is already present.
ICC Cricket World Cup 2024 Points Table: Scrapes and displays the current points table of the Cricket World Cup from Cricbuzz.

**Technologies Used**

Java 8+: Utilizes modern features such as Stream API, Optional, and method references to improve code readability and performance.
Spring Boot: Provides dependency injection, RESTful APIs, and data persistence using JPA.
Jsoup: A Java library for parsing HTML to extract and manipulate data.
Spring Data JPA: Manages interaction with the database, handling CRUD operations on the Match entity.

**Endpoints**

**1. Get Live Match Scores**
This service fetches live match scores by scraping data from Cricbuzz and storing or updating match records in the database.
URL: /api/matches/live
Method: GET
Description: Scrapes live match scores, updates the database, and returns a list of matches.

**2. Get All Matches**
This service fetches all match records from the database.
URL: /api/all-matches
Method: GET
Description: Returns a list of all stored matches in the database.

**3. Get CWC 2024 Points Table**
This service scrapes and returns the ICC Cricket World Cup 2024 points table.
URL: /api/matches/points-table
Method: GET
Description: Scrapes the current points table for the ICC Cricket World Cup 2024 and returns it as a list.

**Code Structure**

CricketServiceImpl.java: Contains the core logic for scraping the live match scores and points table. It uses Jsoup to parse the HTML and extract relevant data. It also manages saving and updating the match records in the database.

Match.java: Entity class representing a cricket match. It holds the details like team names, scores, venue, and match status.

MatchRepository.java: Spring Data JPA repository interface for performing database operations on the Match entity.

Key Methods

1. getLiveMatchScores()
Fetches live scores by connecting to Cricbuzz using Jsoup, scrapes relevant data such as teams, scores, and match status, and updates or adds the match details to the database.

3. updateMatch(Match match)
Checks if a match already exists in the database. If it does, updates the existing record; otherwise, saves a new record.

5. getCWC2024PointTable()
Scrapes the points table for the ICC Cricket World Cup 2024 from the Cricbuzz website and returns it in a structured format.


**How It Works** 

The service uses Jsoup to connect to Cricbuzz's live score and points table pages, then parses the HTML to extract match data (e.g., team names, scores, and venue).
The MatchRepository is used to persist the match data into a relational database, allowing the application to update existing records when necessary.
The service provides RESTful endpoints that can be consumed by a front-end application to display live match information and point tables.

