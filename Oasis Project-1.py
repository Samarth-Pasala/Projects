"""
Oasis Project
"""


import pandas as pd
from flask import Flask, request
app = Flask(__name__)

from nba_api.stats.endpoints import playercareerstats
from nba_api.stats.static import players
from nba_api.stats.endpoints import commonallplayers



all_players = commonallplayers.CommonAllPlayers(is_only_current_season=1)

# Get the data frame containing information about all players
all_players_data = all_players.get_data_frames()[0]

# Extract the player names from the data frame
all_players = all_players_data['DISPLAY_FIRST_LAST'].tolist()


def get_id():
    """
    Gets the player id based on the inputted player name, returns a list of 
    the ids for each player
    """
    
    team_ids = []
    player_names = []
        
    while True:
        player_input = input("Enter the name of an NBA player: \n")
        if player_input in all_players:
            player_dictionary = players.find_players_by_full_name(player_input)
            print("Player found:", player_input)
            team_ids.append(player_dictionary[0]["id"])
            player_names.append(player_dictionary[0]["full_name"])
            break 
        else:
            print("Player not found. Please enter a valid NBA player.")
            # Prompt for another input
            continue
 
    return team_ids, player_names

    
def find_player(team_ids):
    """
    Takes in the player id and returns the data frame with all stats from 
    all years played in the NBA

    """
    dataframes = []
    
    for player_ids in team_ids:
        career = playercareerstats.PlayerCareerStats(player_id = player_ids)
        df = career.get_data_frames()[0]
        dataframes.append(df)
        
    return dataframes

                                      


# @app.route("/player", ["GET"])
def get_stats(dataframes):
    """
    Given a list of dataframes returns a dataframe of the averages of each
    statistic that affects players fantasy points for all players
    """

    
    
    for df in dataframes:
        current_season_data = df.iloc[-1]
        games_played = current_season_data['GP']
        points = current_season_data['PTS']
        rebounds = current_season_data['REB']
        assists = current_season_data['AST']
        blocks = current_season_data['BLK']
        steals = current_season_data['STL']
        turnovers = current_season_data['TOV']
        fg_made = current_season_data['FGM']
        fg_attempted = current_season_data['FGA']
        three_pm = current_season_data["FG3M"]

    
        
        data_frame = {
"Points" : [round(points/games_played, 1)], "Rebounds" : [round(rebounds/games_played, 1)],
"Assists" : [round(assists/games_played, 1)], "Blocks" : [round(blocks/games_played, 1)], 
"Steals" : [round(steals/games_played, 1)], "Turnovers" : [round(turnovers/games_played, 1)],
"FG Made" : [round(fg_made/games_played, 1)], "FG Attempted" : [round(fg_attempted/games_played, 1)],
"FG3M" : [round(three_pm/games_played, 1)]

}
    
        df = pd.DataFrame(data_frame)
        
        json_data = df.to_json(orient='records')
        
        
        
    # player = request.json[json_data]
        

    return json_data, df




def find_algo(df):
    
    # How many fantasy points per category

    f_points = 1
    f_steals = 4
    f_assists = 2
    f_rebounds = 1
    f_turnovers = -2
    f_fgm = 2
    f_fga = -1
    f_tpm = 1
    f_blocks = 4

    # Average Fantasy Points Total per Player

    points_total = f_points * (df.loc[0, "Points"])
    steals_total = f_steals * (df.loc[0, "Steals"])
    assists_total = f_assists * (df.loc[0, "Assists"])
    rebounds_total = f_rebounds * (df.loc[0, "Rebounds"])
    turnovers_total = f_turnovers * (df.loc[0, "Turnovers"])
    fgm_total = f_fgm * (df.loc[0, "FG Made"])
    fga_total = f_fga * (df.loc[0, "FG Attempted"])
    tpm_total = f_tpm * (df.loc[0, "FG3M"])
    blocks_total = f_blocks * (df.loc[0, "Blocks"])
    
    avg_fp = points_total + steals_total + assists_total +\
        rebounds_total + turnovers_total + fgm_total + fga_total + tpm_total\
            + blocks_total
            
            
    return round(avg_fp)



        
        
def algorithm(fantasy_p):
    
    algorithm = fantasy_p
    
    return algorithm
        



        
        
        
def main():
    
     # list of team_ids and player names
     team_ids1, player_names1 = get_id()
     team_ids2, player_names2 = get_id()
    
     # Career statistics dataframe
     player_dataframes1 = find_player(team_ids1)
     player_dataframes2 = find_player(team_ids2)
    
    
    
     # Current season statistic dataframe
     season_data1_json = get_stats(player_dataframes1)
     season_data1_df = get_stats(player_dataframes1)[1]
     print(season_data1_json)
     algo_data_1 = find_algo(season_data1_df)
     player_1_algo = algorithm(algo_data_1)
     print(player_1_algo)
     
   
     season_data2_json = get_stats(player_dataframes2)
     season_data2_df = get_stats(player_dataframes2)[1]
     print(season_data2_json)
     algo_data_2 = find_algo(season_data2_df)
     player_2_algo = algorithm(algo_data_2)
     print(player_2_algo)
     
     
     
     
     
    



if __name__ == "__main__":
        main()









    
