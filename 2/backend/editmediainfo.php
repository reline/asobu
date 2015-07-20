<?php 
header("Content-Type: text/html; charset=utf-8");

include 'common.php';
doDB();

if($_POST) {
    if(!$_POST['edit_media_song'] || !$_POST['edit_media_type'] || !$_POST['edit_media_link'] || !$_POST['edit_media_id']) {
        echo "Editing MEDIA - Check required fields";
        exit();
    }
    
    $safe_media_id = mysqli_real_escape_string($mysqli, $_POST['edit_media_id']);
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['edit_media_song']);
    $safe_link = mysqli_real_escape_string($mysqli, $_POST['edit_media_link']);
    $safe_type = mysqli_real_escape_string($mysqli, $_POST['edit_media_type']);
    
    $edit_media_query = "UPDATE Media SET song_id = '" . $safe_song_id . "', type_of_link = '" . $safe_type . "', link = '" . $safe_link . "' WHERE media_id = " . $safe_media_id;
    
    $edit_media_response = mysqli_query($mysqli, $edit_media_query) or die(mysqli_error($mysqli));
    
    echo "Media $safe_media_id has been edited";
    
    mysqli_free_result($edit_media_response);
    mysqli_close($mysqli);
    
}

?>