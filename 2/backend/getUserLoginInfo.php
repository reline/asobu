<?php

/*
    Gets the user Login info
    
    ...yeah idk what I'm doing in here

*/

header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

if(!$_GET['user_id']) {
    echo '[{error: "No user id found"}]';
    exit();
}

$safe_user_id = "";


if(isset($_GET['user_id'])){
    $safe_user_id = mysqli_real_escape_string($mysqli, $_GET['user_id']);
}


if(!$safe_user_id ) {
    echo '[{error: "you\' not searching for anything"}]';
    exit();
}

$scoreboard_query = <<<QUERY
    SELECT
    Users.user_id. Users.username, Users.phonenumber
    
    FROM Users
    
    WHERE Users.user_id = 
QUERY;

$scoreboard_query .= $safe_user_id;

if($safe_song_id) {
    
    
}



?>