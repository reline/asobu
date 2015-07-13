<?php
include 'common.php';

doDB();

if($_POST) {
    if(!$_POST['input_edit_song_name'] || !$_POST['input_edit_song_difficulty']) {
        echo "Check required fields: adding songs";
        exit();
    }
    
    $safe_name = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_name']);
    $safe_artist = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_artist']);
    $safe_album = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_album']);
    $safe_genre = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_genre']);
    $safe_length = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_length']);
    $safe_diff = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_difficulty']);
    $safe_art = mysqli_real_escape_string($mysqli, $_POST['input_edit_song_art']);
    
    $song_id = $_POST['input_edit_song_id'];
    
    $edit_song_query = "UPDATE Song SET song_name = '".$safe_name."', song_artist = '".$safe_artist."', song_album = '".$safe_album."', song_genre = '" .$safe_genre."' , song_length = '".$safe_length."', song_diff = '".$safe_diff."', album_artwork = '".$song_art."' WHERE song_id = " . $song_id;
    
    $edit_song_response = mysqli_query($mysqli, $edit_song_query) or die(mysqli_error($mysqli));
    
    echo "Song $song_id has been edited";
    
    mysqli_free_result($edit_song_response);
    mysqli_close($mysqli);
    
}

?>