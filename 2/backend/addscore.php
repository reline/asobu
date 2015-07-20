<?php
header("Content-Type: text/html; charset=utf-8");

include 'common.php';

doDB();

if($_POST) {
    
    if(!$_POST['add_user_id'] || !$_POST['add_song_id'] || !$_POST['add_score'] || !$_POST['add_date']) {
        echo "add SCORE - check required fields";
        exit();
    }
    
    $safe_score = mysqli_real_escape_string($mysqli, $_POST['add_score']);
    $safe_achived_date = mysqli_real_escape_string($mysqli, $_POST['add_date']);
    $safe_user_id = mysqli_real_escape_string($mysqli, $_POST['add_user_id']);
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['add_song_id']);
    
    
    $add_score_query = "INSERT INTO Scores (score, achived_date, user_id, song_id) VALUES ('" . $safe_score . "', '" . $safe_achived_date . "', '" . $safe_user_id . "', '" . $safe_song_id . "')";
    
    $add_score_response = mysqli_query($mysqli, $add_score_query) or die(mysqli_error($mysqli));
    
    mysqli_free_result($add_score_response);
    mysqli_close($mysqli);
    
    
}

?>