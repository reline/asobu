<?php
header("Content-Type: text/html; charset=utf-8");
include 'common.php';

doDB();

if($_POST) {
    
    if(!$_POST['edit_lyric_id'] || !$_POST['edit_lyrics_song'] || !$_POST['edit_lyrics_format'] || !$_POST['edit_lyrics_file']) {
        echo "editing LYRICS - Check required fields: adding";
        exit();
    }
    
    $safe_lyric_id = mysqli_real_escape_string($mysqli, $_POST['edit_lyric_id']);
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['edit_lyrics_song']);
    $safe_format = mysqli_real_escape_string($mysqli, $_POST['edit_lyrics_format']);
    $safe_file = mysqli_real_escape_string($mysqli, $_POST['edit_lyrics_file']);
    
    $edit_lyric_query = "UPDATE Lyrics SET song_id = '" . $safe_song_id . "', format = '" . $safe_format . "', link_to_file = '" . $safe_file . "' WHERE lyric_id = " . $safe_lyric_id;
    
    $edit_lyric_response = mysqli_query($mysqli, $edit_lyric_query) or die(mysqli_error($mysqli));
    
    echo "LYRICS - $safe_lyric_id has been edited";
    
    mysqli_free_result($edit_lyric_response);
    mysqli_close($mysqli);
    
    
}

?>