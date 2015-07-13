<?php
include 'common.php';

doDB();

if($_POST) {
    if(!$_POST['input_add_lyrics_format'] || !$_POST['input_add_lyrics_file'] || !$_POST['input_add_lyrics_select_song']) {
        echo "Check required fields: adding lryics";
        exit();
    }
    
    $safe_song_id = mysqli_real_escape_string($mysqli, $_POST['input_add_lyrics_select_song']);
    $safe_lyric_format = mysqli_real_escape_string($mysqli, $_POST['input_add_lyrics_format']);
    $safe_link_to_file = mysqli_real_escape_string($mysqli, $_POST['input_add_lyrics_file']);
    
    $add_lyrics_query = "INSERT INTO Lyrics (song_id, format, link_to_file) VALUES ('".$safe_song_id."', '".$safe_lyric_format."', '".$safe_link_to_file."')";
    
    $add_lyrics_response = mysqli_query($mysqli, $add_lyrics_query) or die(mysqli_error($mysqli));
    
    //get user id
    $lyric_id = mysqli_insert_id($mysqli);
    echo "lyric $lyric_id added";
    
    mysqli_free_result($add_lyrics_response);
    mysqli_close($mysqli);
    
}


?>