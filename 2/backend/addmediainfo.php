<?php
header("Content-Type: text/html; charset=utf-8");
include 'common.php';

doDB();

if($_POST) {
    if(!$_POST['add_media_song'] || !$_POST['add_media_type'] || !$_POST['add_media_link']) {
        echo "Check required fields: adding media";
        exit();
    }
    
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['add_media_song']);
    $safe_media_type = mysqli_real_escape_string($mysqli, $_POST['add_media_type']);
    $safe_link = mysqli_real_escape_string($mysqli, $_POST['add_media_link']);
    
    $add_media_query = "INSERT INTO Media (song_id, type_of_link, link) VALUES ('".$safe_song_id."', '".$safe_media_type."', '".$safe_link."')";
    
    $add_media_response = mysqli_query($mysqli, $add_media_query) or die(mysqli_error($mysqli));
    
    //get user id
    $media_id = mysqli_insert_id($mysqli);
    echo "media $media_id added";
    
    mysqli_free_result($add_media_response);
    mysqli_close($mysqli);
    
}


?>