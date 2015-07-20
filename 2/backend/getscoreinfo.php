<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

if(!isset($_GET['user_id']) && !isset($_GET['song_id']) && !isset($_GET['date']) && !isset($_GET['limit']) && !isset($_GET['score']) && !isset($_GET['orderby']) && !isset($_GET['asc'])){
    echo "SCORES - look at the url and reevaluate your life";
    exit();
}

$safe_user_id = "";
$safe_song_id = "";
$safe_score = "";
$safe_achived_date = "";
$safe_return_limit = "";
$safe_orderby = "";
$safe_asc = "";

if(isset($_GET['user_id'])){
    $safe_user_id = mysqli_real_escape_string($mysqli, $_GET['user_id']);
    //echo "user_id SET!-------------";
}
if(isset($_GET['song_id'])){
    $safe_song_id = mysqli_real_escape_string($mysqli, $_GET['song_id']);
    //echo "song_id SET!-------------";
}
if(isset($_GET['date'])){
    $safe_achived_date = mysqli_real_escape_string($mysqli, $_GET['date']);
    //echo "date SET!-------------";
}
if(isset($_GET['score'])){
    $safe_score = mysqli_real_escape_string($mysqli, $_GET['score']);
    //echo "score SET!-------------";
}
if(isset($_GET['limit'])){
    $safe_return_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
    //echo "limit SET!-------------";
}
if(isset($_GET['orderby'])){
    $safe_orderby = mysqli_real_escape_string($mysqli, $_GET['orderby']);
    //echo "orderby SET!-------------";
}
if(isset($_GET['asc'])){
    $safe_asc = mysqli_real_escape_string($mysqli, $_GET['asc']);
    //echo "asc SET!-------------";
}


if(!$safe_user_id && !$safe_song_id && !$safe_score && !$safe_achived_date && $safe_return_limit){
    echo "SCORES - bruh I'm gonna need more than that...";
    exit();
}

if(!$safe_user_id && !$safe_song_id && !$safe_score && !$safe_achived_date && $safe_orderby){
    echo "SCORES - bruh I'm gonna need more than that...";
    exit();
}

if(!$safe_user_id && !$safe_song_id && !$safe_score && !$safe_achived_date && $safe_asc){
    echo "SCORES - bruh I'm gonna need more than that...";
    exit();
}

if(!$safe_user_id && !$safe_song_id && !$safe_score && !$safe_achived_date){
    echo "SCORES - bruh you're not even searching for anything...";
    exit();
}

$get_score_info_query = "SELECT Scores.song_id, score, song_name, Scores.user_id, username, achived_date FROM Scores, Users, Song WHERE Scores.song_id = Song.song_id AND Scores.user_id = Users.user_id";
$query_string = "";

//join the tables...
if(!$safe_user_id && $safe_song_id){
    //$get_score_info_query = "SELECT Scores.song_id, score, song_name, achived_date FROM Scores, Song WHERE Scores.song_id = Song.song_id";
    $query_string = " AND Scores.song_id='". $safe_song_id ."'";
}
else if($safe_user_id && !$safe_song_id){
    //$get_score_info_query = "SELECT Scores.song_id, score, Scores.user_id, username, achived_date FROM Scores, Users WHERE Scores.user_id = Users.user_id";
    
    $query_string = "  AND Scores.user_id='". $safe_user_id ."'";
}
else if($safe_user_id && $safe_song_id){
    /*
    $get_score_info_query = "SELECT Scores.song_id, score, song_name, Scores.user_id, username, achived_date FROM Scores, Users, Song WHERE Scores.song_id = Song.song_id AND Scores.user_id = Users.user_id";
    */
    
    $query_string = "  AND Scores.user_id='". $safe_user_id ."' AND Scores.song_id='". $safe_song_id ."'";
}
else{
    //$get_score_info_query = "SELECT Scores.song_id, score, achived_date FROM Scores WHERE ";
    $get_score_info_query = "SELECT Scores.song_id, score, song_name, Scores.user_id, username, achived_date FROM Scores, Users, Song WHERE Scores.song_id = Song.song_id AND Scores.user_id = Users.user_id AND";
    
}

//ugh....query stuff

/* but not this stuff...
if($safe_user_id) {
    $query_string .= " AND Scores.user_id='". $safe_user_id ."'";
}
else if(!$query_string && $safe_user_id){
    $query_string .= " Scores.score='". $safe_user_id ."'";
}

if($safe_song_id){
    $query_string .= " AND Scores.song_id='". $safe_song_id ."'";
}
else if(!$query_string && $safe_song_id){
    $query_string .= " Scores.score='". $safe_song_id ."'";
}
*/

if($query_string && $safe_score){
    $query_string .= " AND Scores.score='". $safe_score ."'";
}
else if(!$query_string && $safe_score){
    $query_string .= " Scores.score='". $safe_score ."'";
}

if($query_string && $safe_achived_date){
     $query_string .= " AND Scores.achived_date='". $safe_achived_date ."'";
}
else if(!$query_string && $safe_achived_date){
    $query_string .= " Scores.achived_date='". $safe_achived_date ."'";
}

if($query_string && $safe_return_limit){
    $query_string .= " LIMIT " . $safe_return_limit;
}

if($query_string && $safe_orderby){
    $query_string .= " ORDER BY " . $safe_orderby;
    
    //Doesn't work for some reason
    //TODO: fix this...
    if($safe_asc){
        echo "------------SAFE_ASC------------" . $safe_asc;
        if($safe_asc === 1){
        $query_string .= " ASC";
        }
        else if($safe_asc === 0){
            $query_string .= " DESC";
        }
    }
    
}

/*
if($query_string && $safe_orderby && $safe_ascdesc){
    if($safe_ascdesc == 0){
        $query_string .= " ASC";
    }
    else if($safe_ascdesc == 1){
        $query_string .= " DESC";
    }
    
}
*/

/*
if($safe_user_id && !$safe_song_id) {
    $get_score_info_query = "SELECT song_id, score, achived_date FROM Scores where user_id = '" . $safe_user_id . "'";
}
else if(!$safe_user_id && $safe_song_id) {
    $get_score_info_query = "SELECT user_id, score, achived_date FROM Scores where song_id = '" . $safe_song_id . "'";
}
else if($safe_user_id && $safe_song_id) {
    $get_score_info_query = "SELECT user_id, song_id, score, achived_date FROM Scores where user_id = '". $safe_user_id . "', song_id = '" . $safe_song_id . "'";
    
}
*/

$get_score_info_query .= $query_string;

//echo "query: " . $get_score_info_query . "---------------------------------------";

$get_score_info_response = mysqli_query($mysqli, $get_score_info_query) or die(mysqli_error($mysqli));

$scoreinfo = "[";

while($row = mysqli_fetch_assoc($get_score_info_response)){
    $scoreinfo .= json_encode($row);
    $scoreinfo .= ",";
}
                                                           
$scoreinfo = rtrim($scoreinfo, ",");
$scoreinfo .= "]";
echo $scoreinfo;

mysqli_free_result($get_score_info_response);
mysqli_close($mysqli);
?>