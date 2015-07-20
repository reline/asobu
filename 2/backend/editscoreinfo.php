<?php
header("Content-Type: text/html; charset=utf-8");

include 'common.php';

doDB();

if($_POST) {
    
    if(!$_POST['edit_user_id'] || !$_POST['edit_song_id'] || !$_POST['edit_score'] || !$_POST['edit_date']) {
        
        echo "edit SCORE - check required fields";
        exit();
    }
    
    $safe_user_id = mysqli_real_escape_string($mysqli, $_POST['edit_user_id']);
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['edit_song_id']);
    $safe_score = mysqli_real_escape_string($mysqli, $_POST['edit_score']);
    $safe_achived_date = mysqli_real_escape_string($mysqli, $_POST['edit_date']);
    
    $edit_score_query = "UPDATE Scores SET score = '" . $safe_score . "', achived_date = '" . $safe_achived_date . "' WHERE user_id = '" . $safe_user_id . "' AND song_id = '" . $safe_song_id . "'";
    
    $edit_score_response = mysqli_query($mysqli, $edit_score_query) or die(mysqli_error($mysqli));
    
    mysqli_free_result($edit_score_response);
    mysqli_close($mysqli);
    
}

?>