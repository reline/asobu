<?php

/*
    Gets
    user info -  id
    song info - name, artist, difficulty
    score info - score, date
    
    Takes in
        user id -- required
        song info -- id, name, difficulty
        score

*/

header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

if(!$_GET['user_id']) {
    echo '[{error: "No user id found"}]';
    exit();
}

$safe_user_id = "";

$safe_song_id = "";
$safe_name = "";
$safe_artist  = "";
$safe_diff = "";

$safe_score = "";

$safe_orderby = "";
$safe_asc = "";
$safe_limit = "";

if(isset($_GET['user_id'])){
    $safe_user_id = mysqli_real_escape_string($mysqli, $_GET['user_id']);
}
if(isset($_GET['song_id'])){
    $safe_song_id = mysqli_real_escape_string($mysqli, $_GET['song_id']);
}
if(isset($_GET['name'])) {
    $safe_name = mysqli_real_escape_string($mysqli, $_GET['name']);
}
if(isset($_GET['artist'])) {
    $safe_artist = mysqli_real_escape_string($mysqli, $_GET['artist']);
}
if(isset($_GET['diff'])) {
    $safe_diff = mysqli_real_escape_string($mysqli, $_GET['diff']);
}
if(isset($_GET['score'])){
    $safe_score = mysqli_real_escape_string($mysqli, $_GET['score']);
}
if(isset($_GET['orderby'])) {
    $safe_orderby = mysqli_real_escape_string($mysqli, $_GET['orderby']);
}
if(isset($_GET['asc'])) {
    $safe_asc = mysqli_real_escape_string($mysqli, $_GET['asc']);
}
if(isset($_GET['limit'])) {
    $safe_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
}


if(!$safe_user_id && !$safe_song_id && !$safe_name && !$safe_artist && !$safe_diff && !$safe_score && !$safe_orderby && !$safe_asc && $safe_limit) {
    echo '[{error: "you\' not searching for anything"}]';
    exit();
}

$scoreboard_query = <<<QUERY
    SELECT
    Users.user_id,
    Song.song_id, Song.song_name, Song.song_artist,
    Scores.score, Scores.achived_date
    
    FROM Scores join Song on Scores.song_id = Song.song_id join Users on Scores.user_id = Users.user_id
    
    WHERE Scores.user_id = 
QUERY;

$scoreboard_query .= $safe_user_id;

if($safe_song_id || $safe_name || $safe_artist || $safe_diff || $safe_score || $safe_orderby ||  $safe_asc) {
    //$scoreboard_query .= "";
    $query_string = "";
    /*
    if($query_string && $safe_song_id){
        $query_string .= " AND Song.song_is = '". $safe_song_id ."'";
    }
    
    else if(!$query_string && $safe_song_id){
        $query_string .= " Song.song_is = '". $safe_song_id ."'";
    }
    
    if($query_string && $safe_name){
        $query_string .= " AND Song.song_name LIKE '". $safe_name ."%'";
    }
    else if(!$query_string && $safe_name){
        $query_string .= " Song.song_name LIKE '". $safe_name ."%'";
    }
    
    if($query_string && $safe_diff){
        $query_string .= " AND Song.song_diff = '". $safe_diff ."'";
    }
    else if(!$query_string && $safe_diff){
        $query_string .= " Song.song_diff = '". $safe_diff ."'";
    }
    */
    
    if($safe_song_id){
        $query_string .= " AND Song.song_is = '". $safe_song_id ."'";
    }
    
    if($safe_name){
        $query_string .= " AND Song.song_name LIKE '%". $safe_name ."%'";
    }
    
    if($safe_artist){
        $query_string .= " AND Song.song_artist LIKE '%". $safe_artist ."%'";
    }
    
    if($safe_diff){
        $query_string .= " AND Song.song_diff = '". $safe_diff ."'";
    }
    
    if($safe_score){
        $query_string .= " AND Scores.score LIKE '". $safe_score ."%'";
    }

    if($safe_orderby){
        $query_string .= " ORDER BY ". $safe_orderby;
    }
    
    /*
    if($safe_orderby && $safe_asc){
        $query_string .= " AND ";
    }
    else if(!$query_string && $safe_asc){
        $query_string .= " ";
    }*/
    
    if($safe_limit){
        $query_string .= " LIMIT ". $safe_limit;
    }
    
    
    $scoreboard_query .= $query_string;
    
}


//echo $scoreboard_query . "---------------------------------------";

$scoreboard_response = mysqli_query($mysqli, $scoreboard_query) or die(mysqli_error($mysqli));

$scoreboard_info = "[";

while($row = mysqli_fetch_assoc($scoreboard_response)){
    $scoreboard_info .= json_encode($row);
    $scoreboard_info .= "," ;
}
                                                           
$scoreboard_info = rtrim($scoreboard_info, ",");
$scoreboard_info .= "]";
echo $scoreboard_info;

mysqli_free_result($scoreboard_response);
mysqli_close($mysqli);

?>